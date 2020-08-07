package top.kingworker.blog.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comments.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comments.user_id
     *
     * @mbggenerated
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comments.content
     *
     * @mbggenerated
     */
    @NotEmpty(message = "留言不可以为空")
    private String content;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comments.left_time
     *
     * @mbggenerated
     */
    private Date leftTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comments.article_id
     *
     * @mbggenerated
     */
    @NotNull(message = "不要非法操作")
    private Integer articleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comments.parent_id
     *
     * @mbggenerated
     */
    private Integer parentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column comments.nickname
     *
     * @mbggenerated
     */
    @Size(min = 1,max = 10,message = "给自己一个昵称吧")
    private String nickname;

    private String replyName;

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table comments
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comments.id
     *
     * @return the value of comments.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comments.id
     *
     * @param id the value for comments.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comments.user_id
     *
     * @return the value of comments.user_id
     *
     * @mbggenerated
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comments.user_id
     *
     * @param userId the value for comments.user_id
     *
     * @mbggenerated
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comments.content
     *
     * @return the value of comments.content
     *
     * @mbggenerated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comments.content
     *
     * @param content the value for comments.content
     *
     * @mbggenerated
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comments.left_time
     *
     * @return the value of comments.left_time
     *
     * @mbggenerated
     */
    public Date getLeftTime() {
        return leftTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comments.left_time
     *
     * @param leftTime the value for comments.left_time
     *
     * @mbggenerated
     */
    public void setLeftTime(Date leftTime) {
        this.leftTime = leftTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comments.article_id
     *
     * @return the value of comments.article_id
     *
     * @mbggenerated
     */
    public Integer getArticleId() {
        return articleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comments.article_id
     *
     * @param articleId the value for comments.article_id
     *
     * @mbggenerated
     */
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comments.parent_id
     *
     * @return the value of comments.parent_id
     *
     * @mbggenerated
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comments.parent_id
     *
     * @param parentId the value for comments.parent_id
     *
     * @mbggenerated
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column comments.nickname
     *
     * @return the value of comments.nickname
     *
     * @mbggenerated
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column comments.nickname
     *
     * @param nickname the value for comments.nickname
     *
     * @mbggenerated
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table comments
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", content=").append(content);
        sb.append(", leftTime=").append(leftTime);
        sb.append(", articleId=").append(articleId);
        sb.append(", parentId=").append(parentId);
        sb.append(", nickname=").append(nickname);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}