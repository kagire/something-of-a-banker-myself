package bank.service.elastic;

import bank.model.dto.SearchRequest;
import bank.model.entity.elastic.UserEl;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchService {

    private final ElasticsearchClient esClient;

    public Page<UserEl> searchUsers(SearchRequest request, Pageable pageable) {
        List<Query> mustQueries = new ArrayList<>();

        if (request.getName() != null && !request.getName().isBlank()) {
            mustQueries.add(Query.of(q -> q.prefix(p -> p
                .field("name")
                .value(request.getName())
            )));
        }

        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            mustQueries.add(Query.of(q -> q.term(t -> t
                .field("phones")
                .value(v -> v.stringValue(request.getPhone()))
            )));
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            mustQueries.add(Query.of(q -> q.term(t -> t
                .field("emails")
                .value(v -> v.stringValue(request.getEmail()))
            )));
        }

        if (request.getDateOfBirth() != null) {
            mustQueries.add(Query.of(q -> q.range(r -> r
                .date(rq -> rq
                    .field("dateOfBirth")
                    .gte(request.getDateOfBirth().toString())
                )
            )));
        }

        try {
            SearchResponse<UserEl> response = esClient.search(s -> s
                    .index("user")
                    .from(pageable.getPageNumber() * pageable.getPageSize())
                    .size(pageable.getPageSize())
                    .query(q -> q.bool(b -> b.must(mustQueries))),
                UserEl.class
            );

            List<UserEl> users = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

            long totalHits = response.hits().total() != null ? response.hits().total().value() : users.size();

            return new PageImpl<>(users, pageable, totalHits);

        } catch (Exception e) {
            throw new IllegalArgumentException("cannot do search!");
        }
    }
}
