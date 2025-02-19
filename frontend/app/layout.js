export const metadata = {
    title: "My Next.js App",
    description: "Next.js와 Spring Boot를 연결한 프로젝트",
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
