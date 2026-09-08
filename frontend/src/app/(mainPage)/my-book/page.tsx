import { requestListMyBooks } from "@/actions/book.action";
import { requestListSubject } from "@/actions/subject.action";
import MyBook from "./_component/MyBook";

const MyBookPage = async () => {
    const [books, subjects] = await Promise.all([
        requestListMyBooks(0),
        requestListSubject(0)
    ]);

    return (
        <MyBook
            initialBooks={books.content}
            initialPage={books.page}
            initialTotalPages={books.totalPages}
            initialTotalElements={books.totalElements}
            subjects={subjects.content}
            initialSubjectPage={subjects.page}
            initialSubjectTotalPages={subjects.totalPages}
            loadSubjects={requestListSubject}
        />
    );
};

export default MyBookPage;
