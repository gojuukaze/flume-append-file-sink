package cn.ikaze.flume.sink;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.conf.ConfigurationException;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by linanxing on 2017/5/15.
 */
public class AppendFile extends AbstractSink implements Configurable {
    private static final Logger logger = LoggerFactory.getLogger(AppendFile.class);
    String fileName;
    String appendToolDir;
    String appendTool;
    private static final long defaultBatchSize=100;
    long batchSize;


    public void configure(Context context) {
        String fileName = context.getString("sink.fileName");
        if (fileName == null || fileName.equals(""))
            throw new ConfigurationException("sink.fileName must be specified");

        String appendToolDir = context.getString("sink.appendToolDir");
        if (appendToolDir == null || appendToolDir.equals(""))
            throw new ConfigurationException("sink.appendToolDir must be specified");
        long batchSize = context.getLong("sink.batchSize", defaultBatchSize);

        this.batchSize=batchSize;
        this.fileName = fileName;
        this.appendToolDir = appendToolDir;

    }

    @Override
    public synchronized void start() {
        logger.info("start {}...", this);
        super.start();

        logger.info("appendToolDir {}...", appendToolDir);
        File dir = new File(this.appendToolDir);
        if (!dir.exists()) {
            logger.info("create dir");
            dir.mkdirs();
        }
        else logger.info("dir exists");
        File tool = new File(dir, "append_tool.sh");
        logger.info("appendTool {}...", tool.getAbsolutePath());
        if (!tool.exists()) {
            logger.info("create tool");
            try {
                tool.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileWriter out = null;
            String cmd = "echo $1 >> $2";
            try {
                out = new FileWriter(tool);
                out.write(cmd);
                logger.info("create tool ok");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null)
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
        else logger.info("tool exists");
        this.appendTool = tool.getAbsolutePath();

    }

    @Override
    public synchronized void stop() {
        logger.info("stop {}...", this);

        super.stop();

    }

    public Status process() throws EventDeliveryException {

        Status result = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();
        Event event = null;
        transaction.begin();
        try {
            int txnEventCount = 0;
            for (txnEventCount = 0; txnEventCount < batchSize; txnEventCount++) {
                event = channel.take();
                if (event == null) {
                    break;
                }

                String s = new String(event.getBody());
                if (logger.isInfoEnabled()) {
                    logger.info("{Event.body}: " + s);
                    logger.info("bash {} {Event.body} >> {}" ,this.appendTool, this.fileName);
                }
                Process p = Runtime.getRuntime().exec(new String []{"bash",this.appendTool,s,this.fileName});
                if (p.waitFor() != 0)
                    throw new Exception(String.format("error when run cmd: bash %s {Event.body} %s", this.appendTool, this.fileName));
            }

            transaction.commit();

            if (txnEventCount >0) {
                result= Status.READY;
            }else {
                // No event found, request back-off semantics from the sink runner
                result = Status.BACKOFF;
            }

        } catch (Exception ex) {
            transaction.rollback();
            throw new EventDeliveryException("Failed to log event: " + event, ex);
        } finally {
            transaction.close();
        }

        return result;

    }

}
