package com.innogrid.statemachine;

public enum States {

    // 메인 상태
    INITIAL,
    REQUEST,
    DEVELOPMENT,
    TEST,
    REVIEW,
    BUILD,
    DEPLOY,
    CANCEL,
    END,

    // 요청 서브스테이트
    REQUEST_PENDING_APPROVAL,
    REQUEST_APPROVED,
    REQUEST_REJECTED,

    // 개발 서브스테이트
    DEVELOPMENT_IN_PROGRESS,
    DEVELOPMENT_COMPLETE,
    TEST_IN_PROGRESS,

    TERMINATE,
}
