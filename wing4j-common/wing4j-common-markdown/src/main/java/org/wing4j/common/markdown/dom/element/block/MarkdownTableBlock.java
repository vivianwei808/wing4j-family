package org.wing4j.common.markdown.dom.element.block;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 */
@Data
@ToString(callSuper = true)
@Builder
@EqualsAndHashCode(callSuper = true)
public class MarkdownTableBlock extends AbstractMarkdownBlock {
    int columonNumber = 0;
    final List<String[]> datas = new ArrayList<>();
}
