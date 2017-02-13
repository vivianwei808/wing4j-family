package org.wing4j.common.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.wing4j.common.utils.StringUtils;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
public class PrintZookeeperTree extends ConnectionWatcher {
    private static final Charset CHARSET = Charset.forName("UTF-8");

    public PrintZookeeperTree() {
        super(true);
    }

    public void list(String groupName) throws KeeperException, InterruptedException {

        try {

            StringBuffer sb = new StringBuffer();

            int pathLength = StringUtils.countMatches(groupName, "/");
            for (int i = 0; i < pathLength - 1; ++i) {
                sb.append("\t");
            }

            if (groupName != "/") {
                String node = StringUtils.substringAfterLast(groupName, "/");
                sb.append("|----" + node);
                Stat stat = new Stat();
                byte[] data = zk.getData(groupName, null, stat);

                if (data != null) {
                    sb.append("\t" + new String(data, CHARSET));
                }
                if (stat != null) {
                    sb.append("\t" + stat.getEphemeralOwner());
                }
            } else {
                sb.append(groupName);
            }

            System.out.println(sb.toString());

            List<String> children = zk.getChildren(groupName, false);
            for (String child : children) {
                if (groupName != "/") {
                    list(groupName + "/" + child);
                } else {
                    list(groupName + child);
                }
            }

        } catch (KeeperException.NoNodeException e) {
            log.info("Group %s does not exist\n", groupName);
        }
    }
}