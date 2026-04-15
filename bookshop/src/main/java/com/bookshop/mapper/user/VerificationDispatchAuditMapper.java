package com.bookshop.mapper.user;

import com.bookshop.entity.user.VerificationDispatchAudit;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 验证码发送回执审计 Mapper。
 */
@Mapper
public interface VerificationDispatchAuditMapper {

    @Insert("""
            INSERT INTO verification_dispatch_audit(
                target, delivery_id, channel, code_masked, is_mock, success, error_message, created_at
            ) VALUES(
                #{target}, #{deliveryId}, #{channel}, #{codeMasked}, #{isMock}, #{success}, #{errorMessage}, NOW()
            )
            """)
    int insert(VerificationDispatchAudit audit);

    @Select("""
            <script>
            SELECT id, target, delivery_id, channel, code_masked, is_mock, success, error_message, created_at
            FROM verification_dispatch_audit
            <where>
                <if test="target != null and target != ''">
                    target = #{target}
                </if>
                <if test="success != null">
                    AND success = #{success}
                </if>
                <if test="fromTime != null">
                    AND created_at <![CDATA[ >= ]]> #{fromTime}
                </if>
                <if test="toTime != null">
                    AND created_at <![CDATA[ <= ]]> #{toTime}
                </if>
            </where>
            ORDER BY id DESC
            LIMIT #{limit}
            </script>
            """)
    List<VerificationDispatchAudit> selectRecent(
            @Param("limit") int limit,
            @Param("target") String target,
            @Param("success") Boolean success,
            @Param("fromTime") LocalDateTime fromTime,
            @Param("toTime") LocalDateTime toTime);
}
