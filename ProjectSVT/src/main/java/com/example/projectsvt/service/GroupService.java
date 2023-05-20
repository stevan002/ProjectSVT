package com.example.projectsvt.service;

import com.example.projectsvt.dto.group.CreateGroupDto;
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
}
