package com.spring.model;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.model.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "GROUPS1")
public class Group implements Serializable {
    private long id;
    private String name;
 
    private Set<UserGroup> userGroups = new HashSet<UserGroup>();
     
    public Group() {
    }
 
    public Group(String name) {
        this.name = name;
    }
         
    @Id
    @GeneratedValue
    @Column(name = "GROUP_ID1")
    public long getId() {
        return id;
    }
 
    public void setId(long id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    @JsonIgnore
    @OneToMany(mappedBy = "group",fetch= FetchType.EAGER,
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
		return "Group [name=" + name + "]";
	}
    
}