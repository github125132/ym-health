package com.ymdjk.module.content;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ymdjk.module.content.entity.Content;
import com.ymdjk.module.content.mapper.ContentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentMapper contentMapper;
    public List<Content> list() {
        return contentMapper.selectList(new LambdaQueryWrapper<Content>().eq(Content::getStatus, 1));
    }
}
