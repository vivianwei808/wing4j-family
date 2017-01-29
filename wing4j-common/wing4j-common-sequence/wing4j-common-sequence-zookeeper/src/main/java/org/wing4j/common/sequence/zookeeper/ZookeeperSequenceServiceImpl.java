package org.wing4j.common.sequence.zookeeper;

import lombok.Data;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.data.Stat;
import org.wing4j.common.sequence.SequenceService;
import org.wing4j.common.utils.MessageFormatter;

/**
 * Created by woate on 2017/1/12.
 */
@Data
public class ZookeeperSequenceServiceImpl implements SequenceService {
    String address;
    String port;
    int sessionTimeout;
    int connectionTimeout;
    public static final String SEQ_ZNODE = "/sequence/{}/{}/{}";
    @Override
    public int nextval(String schema, String prefix, String sequenceName, String feature) {
        ZkClient zkClient = new ZkClient(MessageFormatter.format("{}:{}", address, port), sessionTimeout, connectionTimeout);
        String node = MessageFormatter.format(SEQ_ZNODE, schema, sequenceName, feature);
        Stat stat = zkClient.writeDataReturnStat(node, new byte[0], -1);
        int sequence = stat.getVersion();
        zkClient.close();
        return sequence;
    }

    @Override
    public int curval(String schema, String prefix, String sequenceName, String feature) {
        ZkClient zkClient = new ZkClient(MessageFormatter.format("{}:{}", address, port), sessionTimeout, connectionTimeout);
        String node = MessageFormatter.format(SEQ_ZNODE, schema, sequenceName, feature);
        Stat stat = new Stat();
        byte[] sequenceBytes = zkClient.(node, stat);
        String s = new String(sequenceBytes);
        int sequence = Integer.parseInt(s);
        zkClient.close();
        return sequence;
    }
}
