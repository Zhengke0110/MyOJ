package fun.timu.doj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.timu.doj.model.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
