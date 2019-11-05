package com.cipher.cipher.dao;
import com.cipher.cipher.entity.Sentence;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface SentenceDao extends CrudRepository<Sentence,Integer> {
    public Sentence findByText(String text);
    @Override
    public List<Sentence> findAll();
}
