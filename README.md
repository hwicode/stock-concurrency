# 재고시스템으로 알아보는 동시성이슈 해결방법

+ 최상용님의 강의를 보고 따라해본 내용입니다.
+ [강의 링크](https://www.inflearn.com/course/%EB%8F%99%EC%8B%9C%EC%84%B1%EC%9D%B4%EC%8A%88-%EC%9E%AC%EA%B3%A0%EC%8B%9C%EC%8A%A4%ED%85%9C)

<br/>

## 동시성 문제 해결 방법

### [v1] 동시성 고려 X -> @Transactional 메서드에서 재고 감소 호출
+ 재고와 재고 감소 로직 구현
+ 동시에 재고 감소 요청이 들어오면 동시성 문제 발생

<br/>

### [v2] synchronized
+ 메서드에 synchronized 사용
+ synchronized는 하나의 프로세스에만 적용됨 (분산 환경에서 사용 X)
+ 조회되는 엔티티에 상관없이 메서드 단위로 잠금이 걸려서 성능이 매우 떨어짐

<br/>

### [v3] pessimistic lock
+ 충돌이 잦은 경우 optimistic lock보다 성능이 좋음
+ 동시성을 강력하게 지키므로 속도가 느리고 데드락 위험이 있음

<br/>

### [v4] optimistic lock
+ version 컬럼을 통헤 동시성을 관리함 (CAS 방식)
+ DB에 락을 안 걸기 때문에 충돌이 없으면 성능이 좋음
+ version 충돌 시 재시도 로직을 구현해야함
+ 스핀락으로 인한 busy waiting 조심

<br/>

### [v5] named lock
+ 하나의 세션에서 네임드 락을 획득하면 다른 세션에서는 획득 불가
+ 메타 데이터 잠금을 사용함
+ 하나의 세션에서 같은 이름의 락을 여러 번 호출 가능
+ 락을 명시적으로 해제해야함
+ 네임드 락을 위한 DataSource를 추가해야함 -> 커넥션 고갈 방지를 위해 커넥션 풀 분리

<br/>

### [v6] Redis Luttuce
+ 스핀 락과 setnx를 활용하여 분산 락 획득
+ setnx(SET if Not Exists) : 특정 키가 존재하지 않을 경우에만 값을 설정 (원자적으로 작업을 수행)
+ 스핀락으로 인한 busy waiting 조심

<br/>

### [v7] Redis Redisson
+ pub/sub 구조이기에 스핀락에 비해 부하가 줄어듦
+ Redisson 라이브러리를 정확히 이해하고 사용해야함
+ Redisson Lock은 잠금 해제 시간을 충분히 잡아야함
+ Lock 재시도가 필요 X -> Luttuce
+ Lock 재시도가 필요 -> Redisson

