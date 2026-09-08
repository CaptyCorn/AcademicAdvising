import { requestCreateMajor, requestListMajor } from "@/actions/major.action";
import MajorManagement from "./_component/MajorManagement";

const MajorManagementPage = async () => {
	const result = await requestListMajor(0);

	return (
		<MajorManagement
			initialMajors={result.content}
			initialPage={result.page}
			initialTotalPages={result.totalPages}
			initialTotalElements={result.totalElements}
			loadMajors={requestListMajor}
			createMajor={requestCreateMajor}
		/>
	);
};

export default MajorManagementPage;
