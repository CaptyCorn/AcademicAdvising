import { requestListMajor } from "@/actions/major.action";
import { requestCreateSubject, requestListSubject } from "@/actions/subject.action";
import SubjectManagement from "./_component/SubjectManagement";

const SubjectManagementPage = async () => {
	const [subjects, majors] = await Promise.all([
		requestListSubject(0),
		requestListMajor(0)
	]);

	return (
		<SubjectManagement
			initialSubjects={subjects.content}
			initialPage={subjects.page}
			initialTotalPages={subjects.totalPages}
			initialTotalElements={subjects.totalElements}
			initialMajors={majors.content}
			initialMajorPage={majors.page}
			initialMajorTotalPages={majors.totalPages}
			loadSubjects={requestListSubject}
			loadMajors={requestListMajor}
			createSubject={requestCreateSubject}
		/>
	);
};

export default SubjectManagementPage;
