// import React, { useEffect, useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
// import './CategoryAdminMain.css';
//
// const CategoryAdminMain = () => {
//     const [categories, setCategories] = useState([]);
//     const [currentPage, setCurrentPage] = useState(1); // 현재 페이지를 추적하는 상태 변수 추가
//     const [categoriesPerPage] = useState(5); // 페이지당 카테고리 개수를 설정 (고정값으로 5개 설정)
//     const navigate = useNavigate();
//
//     // 카테고리 목록 조회
//     const fetchCategories = async () => {
//         try {
//             const response = await axios.get('http://localhost:8080/api/categories', {
//                 headers: {
//                     'Authorization': `Bearer ${localStorage.getItem('token')}`
//                 },
//             });
//             setCategories(response.data);
//         } catch (error) {
//             console.error('카테고리 목록을 가져오는 데 실패했습니다.', error);
//             if (error.response && error.response.status === 401) {
//                 alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//             }
//         }
//     };
//
//     // 카테고리 삭제 함수
//     const deleteCategory = async (categoryId) => {
//         const confirmDelete = window.confirm("이 카테고리를 삭제하시겠습니까?");
//         if (!confirmDelete) return;
//
//         try {
//             await axios.delete(`http://localhost:8080/api/admin/categories/${categoryId}`, {
//                 headers: {
//                     'Authorization': `Bearer ${localStorage.getItem('token')}`
//                 }
//             });
//             setCategories(categories.filter(category => category.id !== categoryId)); // 삭제 후 상태 업데이트
//             alert("카테고리가 삭제되었습니다.");
//         } catch (error) {
//             console.error('카테고리 삭제 중 오류가 발생했습니다.', error);
//             if (error.response && error.response.status === 401) {
//                 alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//             }
//         }
//     };
//
//     // 카테고리 생성 버튼 핸들러
//     const handleCreateCategory = () => {
//         navigate('/category-create');
//     };
//
//     // 카테고리 수정 버튼 핸들러
//     const handleUpdateCategory = (categoryId) => {
//         navigate(`/category-update/${categoryId}`);
//     };
//
//     // 페이지 변경 함수 추가
//     const handlePageChange = (pageNumber) => {
//         setCurrentPage(pageNumber);
//     };
//
//     // 컴포넌트가 마운트될 때 카테고리 목록을 가져오기 위해 useEffect 안에서 호출합니다.
//     useEffect(() => {
//         fetchCategories();
//     }, []);
//
//     // 페이지네이션을 위한 현재 페이지에 해당하는 카테고리 데이터 계산
//     const indexOfLastCategory = currentPage * categoriesPerPage;
//     const indexOfFirstCategory = indexOfLastCategory - categoriesPerPage;
//     const currentCategories = categories.slice(indexOfFirstCategory, indexOfLastCategory);
//
//     // 전체 페이지 수 계산
//     const totalPages = Math.ceil(categories.length / categoriesPerPage);
//
//     return (
//         <div className="category-admin-container">
//             <h1>카테고리 관리자 메인 페이지</h1>
//             <button className="create-button" onClick={handleCreateCategory}>카테고리 생성</button>
//             <ul className="category-list">
//                 {currentCategories.map(category => (
//                     <li className="category-item" key={category.id}>
//                         {category.name}
//                         <button onClick={() => handleUpdateCategory(category.id)}>수정</button>
//                         <button onClick={() => deleteCategory(category.id)} className="delete-button">삭제</button>
//                     </li>
//                 ))}
//             </ul>
//
//             {/* 페이지네이션 버튼 렌더링 */}
//             <div className="pagination">
//                 <button
//                     onClick={() => handlePageChange(currentPage - 1)}
//                     disabled={currentPage === 1}
//                 >
//                     이전
//                 </button>
//                 <span>{currentPage} / {totalPages}</span>
//                 <button
//                     onClick={() => handlePageChange(currentPage + 1)}
//                     disabled={currentPage === totalPages}
//                 >
//                     다음
//                 </button>
//             </div>
//         </div>
//     );
// };
//
// export default CategoryAdminMain;

// import React, { useEffect, useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
// import './CategoryAdminMain.css';
//
// const CategoryAdminMain = () => {
//     const [categories, setCategories] = useState([]);
//     const [currentPage, setCurrentPage] = useState(1);
//     const [categoriesPerPage] = useState(5);
//     const navigate = useNavigate();
//
//     // 카테고리 목록 조회
//     const fetchCategories = async () => {
//         try {
//             const response = await axios.get('http://localhost:8080/api/categories', {
//                 headers: {
//                     'Authorization': `Bearer ${localStorage.getItem('token')}`
//                 },
//             });
//             setCategories(response.data);
//         } catch (error) {
//             console.error('카테고리 목록을 가져오는 데 실패했습니다.', error);
//             if (error.response && error.response.status === 401) {
//                 alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//             }
//         }
//     };
//
//     // 카테고리 삭제 함수
//     const deleteCategory = async (categoryId) => {
//         const confirmDelete = window.confirm("이 카테고리를 삭제하시겠습니까?");
//         if (!confirmDelete) return;
//
//         try {
//             await axios.delete(`http://localhost:8080/api/admin/categories/${categoryId}`, {
//                 headers: {
//                     'Authorization': `Bearer ${localStorage.getItem('token')}`
//                 }
//             });
//             setCategories(categories.filter(category => category.id !== categoryId));
//             alert("카테고리가 삭제되었습니다.");
//         } catch (error) {
//             console.error('카테고리 삭제 중 오류가 발생했습니다.', error);
//             if (error.response && error.response.status === 401) {
//                 alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//             }
//         }
//     };
//
//     // 카테고리 생성 버튼 핸들러
//     const handleCreateCategory = () => {
//         navigate('/category-create');
//     };
//
//     // 카테고리 수정 버튼 핸들러
//     const handleUpdateCategory = (categoryId) => {
//         navigate(`/category-update/${categoryId}`);
//     };
//
//     // 페이지 변경 함수
//     const handlePageChange = (pageNumber) => {
//         setCurrentPage(pageNumber);
//     };
//
//     useEffect(() => {
//         fetchCategories();
//     }, []);
//
//     const indexOfLastCategory = currentPage * categoriesPerPage;
//     const indexOfFirstCategory = indexOfLastCategory - categoriesPerPage;
//     const currentCategories = categories.slice(indexOfFirstCategory, indexOfLastCategory);
//     const totalPages = Math.ceil(categories.length / categoriesPerPage);
//
//     return (
//         <div className="category-admin-container">
//             <h1>카테고리 관리자 메인 페이지</h1>
//             <button className="create-button" onClick={handleCreateCategory}>카테고리 생성</button>
//             <table className="category-table">
//                 <thead>
//                 <tr>
//                     <th>수정</th>
//                     <th>삭제</th>
//                     <th>카테고리 이름</th>
//                     <th>CATEGORY_ID</th>
//                     <th>DISPLAY_ORDER</th>
//                     <th>PARENT_ID</th>
//                     <th>하위 카테고리</th>
//                     <th>카테고리 이미지</th>
//                 </tr>
//                 </thead>
//                 <tbody>
//                 {currentCategories.map(category => (
//                     <tr key={category.id}>
//                         <td>
//                             <button onClick={() => handleUpdateCategory(category.id)}>수정</button>
//                         </td>
//                         <td>
//                             <button onClick={() => deleteCategory(category.id)} className="delete-button">삭제</button>
//                         </td>
//                         <td>{category.name}</td>
//                         <td>{category.id}</td>
//                         <td>{category.displayOrder}</td>
//                         <td>{category.parentId ? category.parentId : 'null'}</td>
//                         <td>
//                             {category.childCategories ? category.childCategories.join(', ') : 'x'}
//                         </td>
//                         <td>
//                             {category.imageUrl ? <img src={category.imageUrl} alt={category.name} className="category-image" /> : 'x'}
//                         </td>
//                     </tr>
//                 ))}
//                 </tbody>
//             </table>
//
//             {/* 페이지네이션 */}
//             <div className="pagination">
//                 <button
//                     onClick={() => handlePageChange(currentPage - 1)}
//                     disabled={currentPage === 1}
//                 >
//                     이전
//                 </button>
//                 <span>{currentPage} / {totalPages}</span>
//                 <button
//                     onClick={() => handlePageChange(currentPage + 1)}
//                     disabled={currentPage === totalPages}
//                 >
//                     다음
//                 </button>
//             </div>
//         </div>
//     );
// };
//
// export default CategoryAdminMain;

//
// import React, { useEffect, useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
// import './CategoryAdminMain.css';
//
// const CategoryAdminMain = () => {
//     const [categories, setCategories] = useState([]);
//     const [selectedParentId, setSelectedParentId] = useState(null);
//     const [parentPage, setParentPage] = useState(1);
//     const [childPage, setChildPage] = useState(1);
//     const categoriesPerPage = 5;
//     const navigate = useNavigate();
//
//     const fetchCategories = async () => {
//         try {
//             const response = await axios.get('http://localhost:8080/api/categories', {
//                 headers: {
//                     'Authorization': `Bearer ${localStorage.getItem('token')}`
//                 },
//             });
//             setCategories(response.data);
//         } catch (error) {
//             console.error('카테고리 목록을 가져오는 데 실패했습니다.', error);
//             if (error.response && error.response.status === 401) {
//                 alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//             }
//         }
//     };
//
//     const deleteCategory = async (categoryId) => {
//         const confirmDelete = window.confirm("이 카테고리를 삭제하시겠습니까?");
//         if (!confirmDelete) return;
//
//         try {
//             await axios.delete(`http://localhost:8080/api/admin/categories/${categoryId}`, {
//                 headers: {
//                     'Authorization': `Bearer ${localStorage.getItem('token')}`
//                 }
//             });
//             // 삭제 후 상태 업데이트
//             setCategories(categories.filter(category => category.id !== categoryId));
//             alert("카테고리가 삭제되었습니다.");
//         } catch (error) {
//             console.error('카테고리 삭제 중 오류가 발생했습니다.', error);
//             if (error.response && error.response.status === 401) {
//                 alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//             }
//         }
//     };
//
//     useEffect(() => {
//         fetchCategories();
//     }, []);
//
//     const parentCategories = categories.filter(category => category.parentId === null);
//     const childCategories = selectedParentId
//         ? categories.filter(category => category.parentId === selectedParentId)
//         : [];
//
//     const handleCreateCategory = () => {
//         navigate('/category-create');
//     };
//
//     const handleUpdateCategory = (categoryId) => {
//         navigate(`/category-update/${categoryId}`);
//     };
//
//     const handleParentCategoryClick = (parentId) => {
//         setSelectedParentId(parentId === selectedParentId ? null : parentId);
//         setChildPage(1); // 부모 카테고리를 변경할 때 자식 카테고리 페이지를 초기화
//     };
//
//     // 페이지네이션을 위한 계산
//     const paginatedCategories = (categories, page) => {
//         const startIndex = (page - 1) * categoriesPerPage;
//         return categories.slice(startIndex, startIndex + categoriesPerPage);
//     };
//
//     const totalParentPages = Math.ceil(parentCategories.length / categoriesPerPage);
//     const totalChildPages = Math.ceil(childCategories.length / categoriesPerPage);
//
//     return (
//         <div className="category-admin-container">
//             <h1>카테고리 관리자 메인 페이지</h1>
//             <button className="create-button" onClick={handleCreateCategory}>카테고리 생성</button>
//
//             {/* 부모 카테고리 테이블 */}
//             <h2>부모 카테고리</h2>
//             <table className="category-table">
//                 <thead>
//                 <tr>
//                     <th>수정</th>
//                     <th>삭제</th>
//                     <th>카테고리 이름</th>
//                     <th>카테고리 이미지</th>
//                     <th>DISPLAY_ORDER</th>
//                 </tr>
//                 </thead>
//                 <tbody>
//                 {paginatedCategories(parentCategories, parentPage).map(category => (
//                     <tr key={category.id} onClick={() => handleParentCategoryClick(category.id)}>
//                         <td>
//                             <button onClick={(e) => { e.stopPropagation(); handleUpdateCategory(category.id); }}>수정</button>
//                         </td>
//                         <td>
//                             <button onClick={(e) => { e.stopPropagation(); deleteCategory(category.id); }} className="delete-button">삭제</button>
//                         </td>
//                         <td>{category.name}</td>
//                         <td>
//                             {category.imageUrl ? <img src={category.imageUrl} alt={category.name} className="category-image" /> : 'x'}
//                         </td>
//                         <td>{category.displayOrder}</td>
//                     </tr>
//                 ))}
//                 </tbody>
//             </table>
//             <div className="pagination">
//                 <button onClick={() => setParentPage(Math.max(parentPage - 1, 1))} disabled={parentPage === 1}>이전</button>
//                 <span>{parentPage} / {totalParentPages}</span>
//                 <button onClick={() => setParentPage(Math.min(parentPage + 1, totalParentPages))} disabled={parentPage === totalParentPages}>다음</button>
//             </div>
//
//             {/* 자식 카테고리 테이블 */}
//             {selectedParentId && (
//                 <>
//                     <h2>"{parentCategories.find(cat => cat.id === selectedParentId)?.name}"의 자식 카테고리</h2>
//                     <table className="category-table">
//                         <thead>
//                         <tr>
//                             <th>수정</th>
//                             <th>삭제</th>
//                             <th>카테고리 이름</th>
//                             <th>카테고리 이미지</th>
//                             <th>DISPLAY_ORDER</th>
//                         </tr>
//                         </thead>
//                         <tbody>
//                         {paginatedCategories(childCategories, childPage).map(category => (
//                             <tr key={category.id}>
//                                 <td>
//                                     <button onClick={() => handleUpdateCategory(category.id)}>수정</button>
//                                 </td>
//                                 <td>
//                                     <button onClick={() => deleteCategory(category.id)} className="delete-button">삭제</button>
//                                 </td>
//                                 <td>{category.name}</td>
//                                 <td>
//                                     {category.imageUrl ? <img src={category.imageUrl} alt={category.name} className="category-image" /> : 'x'}
//                                 </td>
//                                 <td>{category.displayOrder}</td>
//                             </tr>
//                         ))}
//                         </tbody>
//                     </table>
//                     <div className="pagination">
//                         <button onClick={() => setChildPage(Math.max(childPage - 1, 1))} disabled={childPage === 1}>이전</button>
//                         <span>{childPage} / {totalChildPages}</span>
//                         <button onClick={() => setChildPage(Math.min(childPage + 1, totalChildPages))} disabled={childPage === totalChildPages}>다음</button>
//                     </div>
//                 </>
//             )}
//         </div>
//     );
// };
//
// export default CategoryAdminMain;


import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './CategoryAdminMain.css';

const CategoryAdminMain = () => {
    const [categories, setCategories] = useState([]);
    const [selectedParentId, setSelectedParentId] = useState(null);
    const [parentPage, setParentPage] = useState(1);
    const [childPage, setChildPage] = useState(1);
    const categoriesPerPage = 5;
    const navigate = useNavigate();

    const fetchCategories = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/categories', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
            });
            setCategories(response.data);
        } catch (error) {
            console.error('카테고리 목록을 가져오는 데 실패했습니다.', error);
            if (error.response && error.response.status === 401) {
                alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
            }
        }
    };

    const deleteCategory = async (categoryId) => {
        // 삭제하려는 카테고리의 하위 카테고리 확인
        const hasChildCategories = categories.some(category => category.parentId === categoryId);

        if (hasChildCategories) {
            alert("자식 카테고리가 있어 삭제가 불가능합니다.");
            return;
        }

        const confirmDelete = window.confirm("이 카테고리를 삭제하시겠습니까?");
        if (!confirmDelete) return;

        try {
            await axios.delete(`http://localhost:8080/api/admin/categories/${categoryId}`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            // 삭제 후 상태 업데이트
            setCategories(categories.filter(category => category.id !== categoryId));
            alert("카테고리가 삭제되었습니다.");
        } catch (error) {
            console.error('카테고리 삭제 중 오류가 발생했습니다.', error);
            if (error.response && error.response.status === 401) {
                alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
            }
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const parentCategories = categories.filter(category => category.parentId === null);
    const childCategories = selectedParentId
        ? categories.filter(category => category.parentId === selectedParentId)
        : [];

    const handleCreateCategory = () => {
        navigate('/category-create');
    };

    const handleUpdateCategory = (categoryId) => {
        navigate(`/category-update/${categoryId}`);
    };

    const handleParentCategoryClick = (parentId) => {
        setSelectedParentId(parentId === selectedParentId ? null : parentId);
        setChildPage(1); // 부모 카테고리를 변경할 때 자식 카테고리 페이지를 초기화
    };

    const paginatedCategories = (categories, page) => {
        const startIndex = (page - 1) * categoriesPerPage;
        return categories.slice(startIndex, startIndex + categoriesPerPage);
    };

    const totalParentPages = Math.ceil(parentCategories.length / categoriesPerPage);
    const totalChildPages = Math.ceil(childCategories.length / categoriesPerPage);

    return (
        <div className="category-admin-container">
            <h1>카테고리 관리자 메인 페이지</h1>
            <button className="create-button" onClick={handleCreateCategory}>카테고리 생성</button>

            {/* 부모 카테고리 테이블 */}
            <h2>부모 카테고리</h2>
            <table className="category-table">
                <thead>
                <tr>
                    <th>수정</th>
                    <th>삭제</th>
                    <th>카테고리 이름</th>
                    <th>카테고리 이미지</th>
                    <th>DISPLAY_ORDER</th>
                </tr>
                </thead>
                <tbody>
                {paginatedCategories(parentCategories, parentPage).map(category => (
                    <tr key={category.id} onClick={() => handleParentCategoryClick(category.id)}>
                        <td>
                            <button onClick={(e) => { e.stopPropagation(); handleUpdateCategory(category.id); }}>수정</button>
                        </td>
                        <td>
                            <button onClick={(e) => { e.stopPropagation(); deleteCategory(category.id); }} className="delete-button">삭제</button>
                        </td>
                        <td>{category.name}</td>
                        <td>
                            {category.imageUrl ? <img src={category.imageUrl} alt={category.name} className="category-image" /> : 'x'}
                        </td>
                        <td>{category.displayOrder}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div className="pagination">
                <button onClick={() => setParentPage(Math.max(parentPage - 1, 1))} disabled={parentPage === 1}>이전</button>
                <span>{parentPage} / {totalParentPages}</span>
                <button onClick={() => setParentPage(Math.min(parentPage + 1, totalParentPages))} disabled={parentPage === totalParentPages}>다음</button>
            </div>

            {/* 자식 카테고리 테이블 */}
            {selectedParentId && (
                <>
                    <h2>"{parentCategories.find(cat => cat.id === selectedParentId)?.name}"의 자식 카테고리</h2>
                    <table className="category-table">
                        <thead>
                        <tr>
                            <th>수정</th>
                            <th>삭제</th>
                            <th>카테고리 이름</th>
                            <th>카테고리 이미지</th>
                            <th>DISPLAY_ORDER</th>
                        </tr>
                        </thead>
                        <tbody>
                        {paginatedCategories(childCategories, childPage).map(category => (
                            <tr key={category.id}>
                                <td>
                                    <button onClick={() => handleUpdateCategory(category.id)}>수정</button>
                                </td>
                                <td>
                                    <button onClick={() => deleteCategory(category.id)} className="delete-button">삭제</button>
                                </td>
                                <td>{category.name}</td>
                                <td>
                                    {category.imageUrl ? <img src={category.imageUrl} alt={category.name} className="category-image" /> : 'x'}
                                </td>
                                <td>{category.displayOrder}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <div className="pagination">
                        <button onClick={() => setChildPage(Math.max(childPage - 1, 1))} disabled={childPage === 1}>이전</button>
                        <span>{childPage} / {totalChildPages}</span>
                        <button onClick={() => setChildPage(Math.min(childPage + 1, totalChildPages))} disabled={childPage === totalChildPages}>다음</button>
                    </div>
                </>
            )}
        </div>
    );
};

export default CategoryAdminMain;
