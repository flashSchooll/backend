package com.flashcard.controller.repeattopic;

import com.flashcard.controller.repeattopic.response.RepeatTopicResponse;
import com.flashcard.model.RepeatTopic;
import com.flashcard.service.RepeatTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/repeat-topic")
@RequiredArgsConstructor
public class RepeatTopicController {

    private final RepeatTopicService repeatTopicService;

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RepeatTopicResponse> save(@RequestParam Long topicId,
                                                    @RequestParam(required = false) LocalDateTime repeatTime) {

        RepeatTopic repeatTopic = repeatTopicService.save(topicId, repeatTime);
        RepeatTopicResponse response = new RepeatTopicResponse(repeatTopic);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{topicId}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<String> delete(@PathVariable Long topicId) {
        repeatTopicService.delete(topicId);

        return ResponseEntity.ok("Başarıyla silindi");
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<List<RepeatTopicResponse>> findAll() {
        List<RepeatTopic> repeatTopics = repeatTopicService.findAllByUserAntTopicId();
        List<RepeatTopicResponse> response = repeatTopics.stream().map(RepeatTopicResponse::new).toList();

        return ResponseEntity.ok(response);
    }

}
