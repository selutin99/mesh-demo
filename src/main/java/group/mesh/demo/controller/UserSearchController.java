package group.mesh.demo.controller;

import group.mesh.demo.domain.dao.User;
import group.mesh.demo.service.UserSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
@Api(tags = "User Search")
public class UserSearchController {

    private final UserSearchService userSearchService;

    @GetMapping(value = "/birthDate")
    @ApiOperation(value = "Find user by birth date", authorizations = {@Authorization(value = "jwtToken")})
    public Page<User> findUsersByBirthDate(@RequestParam(name = "birthDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
                                           @PageableDefault(size=20) Pageable pageable) {
        return userSearchService.findByBirthDate(birthDate, pageable);
    }

    @GetMapping(value = "/phone")
    @ApiOperation(value = "Find user by phone", authorizations = {@Authorization(value = "jwtToken")})
    public Page<User> findUsersByPhone(@RequestParam(name = "phone") String phone, Pageable pageable) {
        return userSearchService.findByPhone(phone, pageable);
    }

    @GetMapping(value = "/email")
    @ApiOperation(value = "Find user by email", authorizations = {@Authorization(value = "jwtToken")})
    public Page<User> findUsersByEmail(@RequestParam(name = "email") String email, Pageable pageable) {
        return userSearchService.findByEmail(email, pageable);
    }

    @GetMapping(value = "/name")
    @ApiOperation(value = "Find user by name", authorizations = {@Authorization(value = "jwtToken")})
    public Page<User> findUsersByName(@RequestParam(name = "name") String name, Pageable pageable) {
        return userSearchService.findByName(name, pageable);
    }
}
