package uk.ac.ox.it;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.jmx.JmxConfigurator;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import javax.management.MBeanServer;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("sunapi")
public class ChattyTest {

    private static AtomicLong counter = new AtomicLong();
    private static final int interval = 1000;

    public static void main(String... args) {
        File jgroupsConfig = new File(args[0]);
        final AtomicBoolean run = new AtomicBoolean(true);
        // Stop of INT.
        Signal.handle(new Signal("USR1"), new SignalHandler() {
            public void handle(Signal signal) {
                run.set(false);

            }
        });
        try {
            final JChannel channel = new JChannel(jgroupsConfig);
            ChannelReceiver receiver = new ChannelReceiver();
            channel.setReceiver(receiver);
            channel.setDiscardOwnMessages(true);
            channel.connect("cluster");
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            JmxConfigurator.registerChannel(channel, mBeanServer, "DefaultDomain:name=JGroups");
            System.out.println("Our Address: "+ channel.getAddress());
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    while(run.get()) {
                        try {
                            Thread.sleep(interval);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            channel.send(new Message(null, null, new TestMessage(counter.getAndIncrement())));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class TestMessage implements Serializable {

        public long number;

        public TestMessage(long number) {
            this.number = number;
        }

    }

    static class ChannelReceiver implements Receiver {

        public void viewAccepted(View new_view) {
            System.out.println("View updated: "+ new_view);

        }

        public void suspect(Address suspected_mbr) {
            System.out.println("Crashed? :"+ suspected_mbr.toString());

        }

        public void block() {

        }

        public void unblock() {

        }

        public void receive(Message msg) {
            Object o = msg.getObject();
            if (o instanceof TestMessage) {
                System.out.println(msg.getSrc() + " "+ ((TestMessage) o).number);
            }
        }

        public void getState(OutputStream output) throws Exception {

        }

        public void setState(InputStream input) throws Exception {

        }
    }
}
