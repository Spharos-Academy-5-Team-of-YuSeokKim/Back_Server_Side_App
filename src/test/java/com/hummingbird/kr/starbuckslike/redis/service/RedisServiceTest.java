package com.hummingbird.kr.starbuckslike.redis.service;

import com.hummingbird.kr.starbuckslike.redis.dto.out.RecentSearchResponseDto;
import com.hummingbird.kr.starbuckslike.redis.facade.ReviewLockFacade;
import com.hummingbird.kr.starbuckslike.review.application.ReviewService;

import com.hummingbird.kr.starbuckslike.review.dto.in.AddReviewCommentRequestDto;
import com.hummingbird.kr.starbuckslike.review.dto.in.DeleteReviewCommentRequestDto;
import com.hummingbird.kr.starbuckslike.review.infrastructure.ReviewRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.hummingbird.kr.starbuckslike.review.domain.QReview.review;
import static com.hummingbird.kr.starbuckslike.review.domain.QReviewComment.reviewComment;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
class RedisServiceTest {
    @Autowired
    RedisService redisService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewLockFacade reviewLockFacade;
    //@Autowired
    //private TestRestTemplate restTemplate;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    //@Commit
    void searchWordTest(){
        String userId = "user1";
        redisService.addSearchWord(userId, "검색어1");
        redisService.addSearchWord(userId, "검색어2");
        redisService.addSearchWord(userId, "검색어3");
        redisService.addSearchWord(userId, "검색어4");
        redisService.addSearchWord(userId, "검색어5");
        redisService.addSearchWord(userId, "검색어6");
        RecentSearchResponseDto res1 = redisService.getRecentSearchDto(userId);
        log.info(res1);

        redisService.deleteSearchWord(userId, "검색어4");
        RecentSearchResponseDto res2 = redisService.getRecentSearchDto(userId);
        log.info(res2);

//        redisService.deleteUserSearchKey(userId);
//        RecentSearchResponseDto res3 = redisService.getRecentSearchDto(userId);
//        log.info(res3);
    }

    @Test
    public void 동시에_100개의_저장요청_레디스락O() throws InterruptedException {
        //3122ms, 4046ms, 3786ms
        // 1442ms 1573ms, 1500ms , 1300ms

        // 200개 : 2054, 2374
        Long key = 2L;
        int threadCount = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    AddReviewCommentRequestDto dto = AddReviewCommentRequestDto.builder()
                                                        .reviewId(key)
                                                        .nickname("동시성 확인")
                                                        .memberUID("zzzz-yyyy")
                                                        .content("좋아좋아")
                                                        .build();
                    reviewLockFacade.addReviewCommentAndIncreaseCount(dto);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        // 검증
        Integer res = reviewRepository.findById(key).orElseThrow().getCommentCount();
        log.info(res);
        assertEquals(threadCount, res);
    }
//    @Test
//    public void 동시에_100개의요청_락X() throws InterruptedException {
//        Long key = 2L;
//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        for (int i = 0; i < threadCount; i++) {
//            executorService.submit(() -> {
//                try {
//                    AddReviewCommentRequestDto dto = AddReviewCommentRequestDto.builder()
//                            .reviewId(key)
//                            .nickname("동시성 확인")
//                            .memberUID("zzzz-yyyy")
//                            .content("좋아좋아")
//                            .build();
//                    reviewService.addReviewComment(dto);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//        // 검증
//        Integer res = reviewRepository.findById(key).orElseThrow().getCommentCount();
//        log.info(res);
//        assertEquals(threadCount, res);
//    }

    @Test
    public void 동시에_100개의요청_레디스락O_삭제() throws InterruptedException {
        List<Long> reviewCommentKey = queryFactory
                .select(reviewComment.id)
                .from(reviewComment)
                .where(reviewComment.memberUID.eq("zzzz-yyyy"))
                .fetch();
        int threadCount = reviewCommentKey.size();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final Long reviewCommentId = reviewCommentKey.get(i);  // i번째 값을 미리 저장해 스레드에서 참조하도록
            executorService.submit(() -> {
                try {
                    DeleteReviewCommentRequestDto dto = DeleteReviewCommentRequestDto
                            .builder()
                            .reviewCommentId(reviewCommentId)  // index를 사용
                            .memberUID("zzzz-yyyy")
                            .build();
                    reviewLockFacade.deleteReviewAndDecreaseCount(dto);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        // 검증
        Integer res = queryFactory
                .select(review.commentCount)
                .from(review)
                .where(review.id.eq(2L))
                .fetchOne();
        log.info(res);
        // 100개에서 100개 삭제했으니 0개
        assertEquals(res, 0);
    }


    @Test
    public void 동시에_100개의_저장요청_비관적락() throws InterruptedException {
        //  1031ms , 963ms , 964ms , 1038
        // 200개 : 1507 , 1275
        Long key = 2L;
        int threadCount = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    AddReviewCommentRequestDto dto = AddReviewCommentRequestDto.builder()
                            .reviewId(key)
                            .nickname("동시성 확인")
                            .memberUID("zzzz-yyyy")
                            .content("좋아좋아")
                            .build();
                    reviewService.increaseCommentCountWithLock(dto);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        // 검증
        Integer res = reviewRepository.findById(key).orElseThrow().getCommentCount();
        log.info(res);
        assertEquals(threadCount, res);
    }



}