package az.ingress.userauthenticationsystem.entity;

import org.springframework.data.annotation.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RedisHash("RefreshToken")
public class RefreshToken {

    @Id
    String id;

    @Indexed
    String username;

    @TimeToLive
    Long ttl;
}
