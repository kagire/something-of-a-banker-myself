package bank.dao.elastic;

import bank.model.entity.elastic.UserEl;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserElRepository extends ElasticsearchRepository<UserEl, Long> {
}
