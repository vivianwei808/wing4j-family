package org.wing4j.orm;

import org.wing4j.orm.count.CountMapper;
import org.wing4j.orm.delete.DeleteMapper;
import org.wing4j.orm.insert.InsertMapper;
import org.wing4j.orm.lock.LockMapper;
import org.wing4j.orm.select.SelectMapper;
import org.wing4j.orm.update.UpdateMapper;

/**
 * Created by wing4j on 2016/12/18.
 */
public interface CrudMapper<T,K>extends
        CountMapper<T,K>,
        SelectMapper<T,K>,
        InsertMapper<T,K>,
        UpdateMapper<T,K>,
        DeleteMapper<T,K>,
        LockMapper<T,K>
{
}
