export const metadata = {
    title: "Marathoners ",
    description: "Next.js와 Spring Boot를 연결하여 공부 및 서비스를 목표로 하는 프로젝트",
};

export default function RootLayout({ children }) {
    return (
        <html lang="ko">
        <head>
            <meta charSet="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        </head>
        <body>
        {children}
        </body>
        </html>
    );
}
