package bank.controller;

import bank.model.dto.SearchRequest;
import bank.model.entity.elastic.UserEl;
import bank.service.elastic.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("search")
@AllArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("user")
    public Page<UserEl> searchUsers(
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "phone", required = false) String phone,
        @RequestParam(name = "email", required = false) String email,
        @RequestParam(name = "dateOfBirth", required = false) LocalDate dateOfBirth,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        SearchRequest request = new SearchRequest(name, dateOfBirth, phone, email);

        return searchService.searchUsers(request, pageable);
    }
}
