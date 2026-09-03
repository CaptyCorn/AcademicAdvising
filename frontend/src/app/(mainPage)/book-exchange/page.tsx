import { requestListBook } from "@/actions/book.action";
import { requestListMajor } from "@/actions/major.action";
import { requestListSubject } from "@/actions/subject.action";
import BookPage from "./_component/BookPage";

const BookExchangePage = async () => {
    const [books, majors, subjects] = await Promise.all([
        requestListBook({ page: 0 }),
        requestListMajor(0),
        requestListSubject(0)
    ]);

    return(
        <BookPage
            books={books.content}
            bookPage={books.page}
            bookTotalPages={books.totalPages}
            bookTotalElements={books.totalElements}
            majors={majors.content}
            majorPage={majors.page}
            majorTotalPages={majors.totalPages}
            subjects={subjects.content}
            subjectPage={subjects.page}
            subjectTotalPages={subjects.totalPages}
            loadBooks={requestListBook}
            loadMoreMajors={requestListMajor}
            loadMoreSubjects={requestListSubject}
        />
    );
}

export default BookExchangePage;