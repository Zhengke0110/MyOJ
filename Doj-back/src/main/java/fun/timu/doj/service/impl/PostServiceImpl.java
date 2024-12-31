package fun.timu.doj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.doj.mapper.PostMapper;
import fun.timu.doj.model.entity.Post;
import fun.timu.doj.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Override
    public void validPost(Post post, boolean add) {

    }
}
