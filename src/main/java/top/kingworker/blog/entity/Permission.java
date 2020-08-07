package top.kingworker.blog.entity;

import java.io.Serializable;

public class Permission implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column permissions.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column permissions.permission
     *
     * @mbggenerated
     */
    private String permission;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column permissions.permission_name
     *
     * @mbggenerated
     */
    private String permissionName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table permissions
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column permissions.id
     *
     * @return the value of permissions.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column permissions.id
     *
     * @param id the value for permissions.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column permissions.permission
     *
     * @return the value of permissions.permission
     *
     * @mbggenerated
     */
    public String getPermission() {
        return permission;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column permissions.permission
     *
     * @param permission the value for permissions.permission
     *
     * @mbggenerated
     */
    public void setPermission(String permission) {
        this.permission = permission == null ? null : permission.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column permissions.permission_name
     *
     * @return the value of permissions.permission_name
     *
     * @mbggenerated
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column permissions.permission_name
     *
     * @param permissionName the value for permissions.permission_name
     *
     * @mbggenerated
     */
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table permissions
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
        sb.append(", permission=").append(permission);
        sb.append(", permissionName=").append(permissionName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}