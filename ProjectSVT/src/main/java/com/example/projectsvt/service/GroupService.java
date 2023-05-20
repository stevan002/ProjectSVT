package com.example.projectsvt.service;

import com.example.projectsvt.dto.group.CreateGroupDto;
import com.example.projectsvt.dto.group.UpdateGroupDto;
import com.example.projectsvt.model.Group;
import com.example.projectsvt.model.User;
import com.example.projectsvt.model.UserGroup;
import com.example.projectsvt.repository.GroupRepository;
import com.example.projectsvt.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserService userService;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    public ResponseEntity<?> createGroup(CreateGroupDto createGroupDto){
        if(createGroupDto.getName() == null || createGroupDto.getName().isEmpty()){
            return new ResponseEntity<>("Name must not be empty", HttpStatus.BAD_REQUEST);
        }
        if(createGroupDto.getCreatedBy() == null){
            return new ResponseEntity<>("UserID must not be empty", HttpStatus.BAD_REQUEST);
        }

        Group group = new Group();

        ResponseEntity<?> findByIdUserResponse = userService.findById(createGroupDto.getCreatedBy());
        if(findByIdUserResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return findByIdUserResponse;
        }

        User user = (User) findByIdUserResponse.getBody();
        group.setCreatedBy(user);
        group.setName(createGroupDto.getName());
        group.setDescription(createGroupDto.getDescription());
        group = groupRepository.save(group);

        UserGroup userGroup = new UserGroup(user, group);
        userGroupRepository.save(userGroup);

        String response = String.format("Group saved successfully ! GroupID : %d", group.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> findById(Long id){
        Optional<Group> optionalGroup = groupRepository.findById(id);

        if(optionalGroup.isEmpty()){
            return new ResponseEntity<>("Group with given ID does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalGroup.get(), HttpStatus.OK);
    }

    public ResponseEntity<?> findGroupsByUser(Long userId) {
        ResponseEntity<?> findByIdUserResponse = userService.findById(userId);
        if(findByIdUserResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return findByIdUserResponse;
        }
        List<UserGroup> userGroups = userGroupRepository.findByUserId(userId);
        if(userGroups.isEmpty()){
            return new ResponseEntity<>("Given User is not a member of any group", HttpStatus.NOT_FOUND);
        }

        List<Group> groups = new ArrayList<>();
        userGroups.forEach(userGroup -> {groups.add(userGroup.getGroup());});

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    public ResponseEntity<?> updateGroup(Long id, UpdateGroupDto updateGroupDto){
        if(updateGroupDto.getName() == null || updateGroupDto.getName().isEmpty()) {
            return new ResponseEntity<>("Name must not be empty", HttpStatus.BAD_REQUEST);
        }

        Optional<Group> optionalGroup = groupRepository.findById(id);
        if(optionalGroup.isEmpty()){
            return new ResponseEntity<>("Group with given ID does not exist", HttpStatus.NOT_FOUND);
        }

        Group group = optionalGroup.get();
        group.setName(updateGroupDto.getName());
        group.setDescription(updateGroupDto.getDescription());
        groupRepository.save(group);

        return new ResponseEntity<>("Group updated successfully !", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteGroup(Long id, Long userId){
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (optionalGroup.isEmpty()){
            return new ResponseEntity<>("Group with given ID does not exist", HttpStatus.NOT_FOUND);
        }

        Group group = optionalGroup.get();
        if(!group.getCreatedBy().getId().equals(userId)){
            return new ResponseEntity<>("Given group was not created by given user", HttpStatus.NOT_FOUND);
        }

        List<UserGroup> userGroups = userGroupRepository.findByGroupId(id);
        for(UserGroup userGroup : userGroups){
            removeUserFromGroup(userGroup.getGroup().getId(), userGroup.getUser().getId());
        }

        groupRepository.delete(group);
        return new ResponseEntity<>("Group successfully deleted !", HttpStatus.OK);
    }

    public ResponseEntity<?> removeUserFromGroup(Long groupId, Long userId){
        ResponseEntity<?> findByIdGroupResponse = findById(groupId);
        if(findByIdGroupResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return findByIdGroupResponse;
        }

        ResponseEntity<?> findByIdUserResponse = userService.findById(userId);
        if(findByIdUserResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return findByIdUserResponse;
        }

        Optional<UserGroup> optionalUserGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        if (optionalUserGroup.isEmpty()){
            return new ResponseEntity<>("User is not a group member", HttpStatus.CONFLICT);
        }

        userGroupRepository.delete(optionalUserGroup.get());
        return new ResponseEntity<>("User successfully removed from group !",  HttpStatus.OK);

    }

    public ResponseEntity<?> addUserToGroup(Long groupId, Long userId){
        ResponseEntity<?> findByIdGroupResponse = findById(groupId);
        if(findByIdGroupResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return findByIdGroupResponse;
        }

        ResponseEntity<?> findByIdUserResponse = userService.findById(userId);
        if(findByIdUserResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return findByIdUserResponse;
        }

        if(userGroupRepository.existsByUserIdAndGroupId(userId, groupId)){
            return new ResponseEntity<>("User is already a group member", HttpStatus.CONFLICT);
        }

        User user = (User) findByIdUserResponse.getBody();
        Group group = (Group) findByIdGroupResponse.getBody();

        UserGroup userGroup = new UserGroup(user, group);
        userGroupRepository.save(userGroup);

        return new ResponseEntity<>("User successfully added to group !", HttpStatus.OK);
    }
}
