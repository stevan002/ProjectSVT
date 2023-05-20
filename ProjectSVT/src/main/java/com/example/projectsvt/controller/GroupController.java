package com.example.projectsvt.controller;

import com.example.projectsvt.dto.group.CreateGroupDto;
import com.example.projectsvt.dto.group.UpdateGroupDto;
import com.example.projectsvt.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupDto createGroupDto){
        return groupService.createGroup(createGroupDto);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findGroupById(@PathVariable("id") Long id){
        return groupService.findById(id);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findGroupByUser(@RequestParam(name = "user") Long userId){
        return groupService.findGroupsByUser(userId);
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGroup(@PathVariable("id") Long id, @RequestBody UpdateGroupDto updateGroupDto) {
        return groupService.updateGroup(id, updateGroupDto);
    }

    @DeleteMapping(path = "/{id}/{user-id}")
    public ResponseEntity<?> deleteGroup(@PathVariable("id") Long id, @PathVariable("user-id") Long userId){
        return groupService.deleteGroup(id, userId);
    }

    @PatchMapping(path = "/{id}/remove/{user-id}")
    public ResponseEntity<?> removeUserFromGroup(@PathVariable("id") Long groupId, @PathVariable("user-id") Long userId){
        return groupService.removeUserFromGroup(groupId, userId);
    }

    @PatchMapping(path = "{id}/add/{user-id}")
    public ResponseEntity<?> addUserToGroup(@PathVariable("id") Long groupId, @PathVariable("user-id") Long userId){
        return groupService.addUserToGroup(groupId, userId);
    }
}
