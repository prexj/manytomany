package com.spring.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USERS1")
public class User implements Serializable {
    private long id;
    private String username;
    private String password;
    private String email;  
 
    private Set<UserGroup> userGroups = new HashSet<UserGroup>();
 
    public User() {
    }
 
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
     
    public void addGroup(UserGroup group) {
        this.userGroups.add(group);
    }
 
    @Id
    @GeneratedValue
    @Column(name = "USER_ID1")
    public long getId() {
        return id;
    }
 
    public void setId(long id) {
        this.id = id;
    }
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch=FetchType.EAGER,
            cascade = CascadeType.ALL)
    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }
 
    public void setUserGroups(Set<UserGroup> groups) {
        this.userGroups = groups;
    }
     
    public void addUserGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
    }

	@Override
	public String toString() {
		return "User [ username=" + username + ", password="
				+ password + ", email=" + email + "]";
	}  
    
}