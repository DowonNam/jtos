package com.korea.jtos.Answer;

import com.korea.jtos.DataNotFoundException;
import com.korea.jtos.Question.Question;
import com.korea.jtos.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer create(Question question, String content, SiteUser author){
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
        return answer;
    }
    public Answer getAnswer(Integer id){
        Optional<Answer> answer = this.answerRepository.findById(id);
        if(answer.isPresent()){
            return answer.get();
        }else{
            throw new DataNotFoundException("answer not found");
        }
    }
    public void modify(Answer answer,String content){
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    public void delete(Answer answer){
        this.answerRepository.delete(answer);
    }

    public void vote(Answer answer,SiteUser siteUser){
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }

    public Page<Answer> getList(int questionId, int page) {
        // 답변을 createDate 기준으로 내림차순 정렬
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        return this.answerRepository.findByQuestionId(questionId, pageable);
    }

    public Page<Answer> getListSortedByVotes(int questionId, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("votesCount").descending());
        return this.answerRepository.findByQuestionId(questionId, pageable);
    }
}
