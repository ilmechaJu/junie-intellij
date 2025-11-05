# scripts

이 디렉토리에는 로드/스트레스 테스트 스크립트를 둡니다.

예시 도구
- k6: 자바스크립트 기반 HTTP 부하 테스트
- Apache JMeter: GUI/CLI 로드 테스트

샘플 k6 스니펫
```
import http from 'k6/http';
import { sleep } from 'k6';
export let options = { vus: 10, duration: '30s' };
export default function () {
  http.get('http://localhost:8080/health');
  sleep(1);
}
```
