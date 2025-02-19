"use client";  // ⬅️ 클라이언트 컴포넌트로 선언

export default function HomePage() {
    return (
        <div>
            <h1>Next.js 홈페이지</h1>
            <p>이 페이지는 `index.html` 역할을 합니다.</p>

            <style jsx>{`
        div {
          text-align: center;
          padding: 50px;
        }
      `}</style>
        </div>
    );
}
