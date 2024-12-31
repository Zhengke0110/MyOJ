package fun.timu.doj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.timu.doj.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
