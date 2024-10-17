// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';
// import './categoryCreate.css';
//
// const CategoryCreate = () => {
//     const [name, setName] = useState('');
//     const [displayOrder, setDisplayOrder] = useState('');
//     const [parentId, setParentId] = useState(null);
//     const [parentCategories, setParentCategories] = useState([]); // 부모 카테고리만 저장
//
//     const navigate = useNavigate();
//
//     useEffect(() => {
//         // 서버에서 카테고리 목록 가져오기
//         axios.get('http://localhost:8080/api/categories', {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 // parentId가 null인 카테고리만 필터링
//                 const parentCategories = response.data.filter(category => category.parentId === null);
//                 setParentCategories(parentCategories);
//             })
//             .catch(error => {
//                 console.error('Error fetching categories:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     }, []);
//
//     const handleSubmit = (e) => {
//         e.preventDefault();
//
//         const categoryData = {
//             name,
//             displayOrder: displayOrder ? Number(displayOrder) : null,
//             parentId
//         };
//
//         axios.post('http://localhost:8080/api/admin/categories', categoryData, {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 console.log('Category created:', response.data);
//                 navigate('/admin/categories'); // 성공적으로 생성된 후 메인 화면으로 이동
//             })
//             .catch(error => {
//                 console.error('Error creating category:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     };
//
//     return (
//         <div className="category-create-container">
//             <h2>카테고리 생성</h2>
//             <form onSubmit={handleSubmit}>
//                 <div className="form-group">
//                     <label>이름:</label>
//                     <input
//                         type="text"
//                         value={name}
//                         onChange={(e) => setName(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>표시 순서:</label>
//                     <input
//                         type="number"
//                         value={displayOrder}
//                         onChange={(e) => setDisplayOrder(e.target.value)}
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>부모 카테고리 선택:</label>
//                     <select
//                         value={parentId || ''}
//                         onChange={(e) => setParentId(e.target.value || null)}
//                     >
//                         <option value="">부모 카테고리 없음</option>
//                         {parentCategories.map((category) => (
//                             <option key={category.id} value={category.id}>
//                                 {category.name}
//                             </option>
//                         ))}
//                     </select>
//                 </div>
//                 <button type="submit" className="submit-button">카테고리 생성</button>
//             </form>
//         </div>
//     );
// };
//
// export default CategoryCreate;


// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';
//
// const CategoryCreate = () => {
//     const [name, setName] = useState('');
//     const [displayOrder, setDisplayOrder] = useState('');
//     const [parentId, setParentId] = useState(null);
//     const [parentCategories, setParentCategories] = useState([]); // 부모 카테고리만 저장
//     const [image, setImage] = useState(null); // 이미지 상태 추가
//
//     const navigate = useNavigate();
//
//     useEffect(() => {
//         // 서버에서 카테고리 목록 가져오기
//         axios.get('http://localhost:8080/api/categories', {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 const parentCategories = response.data.filter(category => category.parentId === null);
//                 setParentCategories(parentCategories);
//             })
//             .catch(error => {
//                 console.error('Error fetching categories:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     }, []);
//
//     const handleImageChange = (e) => {
//         setImage(e.target.files[0]);
//     };
//
//     const handleSubmit = (e) => {
//         e.preventDefault();
//
//         const categoryData = {
//             name,
//             displayOrder: displayOrder ? Number(displayOrder) : null,
//             parentId
//         };
//
//         axios.post('http://localhost:8080/api/admin/categories', categoryData, {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 const categoryId = response.data.id;
//                 if (image) {
//                     uploadCategoryImage(categoryId);
//                 } else {
//                     navigate('/admin/categories');
//                 }
//             })
//             .catch(error => {
//                 console.error('Error creating category:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     };
//
//     // const uploadCategoryImage = (categoryId) => {
//     //     const formData = new FormData();
//     //     formData.append('image', image);
//     //     console.log("토큰 값:", token); // 토큰 값을 콘솔에 출력
//     //
//     //     axios.post(`http://localhost:8080/api/admin/categories/${categoryId}/image`, formData, {
//     //         headers: {
//     //             'Authorization': `Bearer ${localStorage.getItem('token')}`,
//     //             // 'Content-Type': 'multipart/form-data'
//     //         }
//     //     })
//     //         .then(() => {
//     //             navigate('/admin/categories');
//     //         })
//     //         .catch(error => {
//     //             console.error('Error uploading image:', error);
//     //             alert("이미지 업로드에 실패했습니다.");
//     //         });
//     // };
//     const uploadCategoryImage = (categoryId) => {
//         const token = localStorage.getItem('token'); // token 변수를 선언하고 값을 가져옴
//         console.log("토큰 값:", token); // 콘솔에 토큰 값 출력
//
//         const formData = new FormData();
//         formData.append('image', image);
//
//         axios.post(`http://localhost:8080/api/admin/categories/${categoryId}/image`, formData, {
//             headers: {
//                 'Authorization': `Bearer ${token}`, // 토큰을 Authorization 헤더에 추가
//                 // 'Content-Type': 'multipart/form-data' // Axios가 자동으로 설정
//             }
//         })
//             .then(() => {
//                 navigate('/admin/categories');
//             })
//             .catch(error => {
//                 console.error('Error uploading image:', error);
//                 alert("이미지 업로드에 실패했습니다.");
//             });
//     };
//
//
//     return (
//         <div className="category-create-container">
//             <h2>카테고리 생성</h2>
//             <form onSubmit={handleSubmit}>
//                 <div className="form-group">
//                     <label>이름:</label>
//                     <input
//                         type="text"
//                         value={name}
//                         onChange={(e) => setName(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>표시 순서:</label>
//                     <input
//                         type="number"
//                         value={displayOrder}
//                         onChange={(e) => setDisplayOrder(e.target.value)}
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>부모 카테고리 선택:</label>
//                     <select
//                         value={parentId || ''}
//                         onChange={(e) => setParentId(e.target.value || null)}
//                     >
//                         <option value="">부모 카테고리 없음</option>
//                         {parentCategories.map((category) => (
//                             <option key={category.id} value={category.id}>
//                                 {category.name}
//                             </option>
//                         ))}
//                     </select>
//                 </div>
//                 <div className="form-group">
//                     <label>카테고리 이미지:</label>
//                     <input
//                         type="file"
//                         accept="image/*"
//                         onChange={handleImageChange}
//                     />
//                 </div>
//                 <button type="submit" className="submit-button">카테고리 생성</button>
//             </form>
//         </div>
//     );
// };
//
// export default CategoryCreate;


// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';
//
// const CategoryCreate = () => {
//     const [name, setName] = useState('');
//     const [displayOrder, setDisplayOrder] = useState('');
//     const [parentId, setParentId] = useState(null);
//     const [parentCategories, setParentCategories] = useState([]); // 부모 카테고리만 저장
//
//     const navigate = useNavigate();
//
//     useEffect(() => {
//         // 서버에서 카테고리 목록 가져오기
//         axios.get('http://localhost:8080/api/categories', {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 const parentCategories = response.data.filter(category => category.parentId === null);
//                 setParentCategories(parentCategories);
//             })
//             .catch(error => {
//                 console.error('Error fetching categories:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     }, []);
//
//     const handleSubmit = (e) => {
//         e.preventDefault();
//
//         const categoryData = {
//             name,
//             displayOrder: displayOrder ? Number(displayOrder) : null,
//             parentId
//         };
//
//         axios.post('http://localhost:8080/api/admin/categories', categoryData, {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(() => {
//                 navigate('/admin/categories');
//             })
//             .catch(error => {
//                 console.error('Error creating category:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     };
//
//     return (
//         <div className="category-create-container">
//             <h2>카테고리 생성</h2>
//             <form onSubmit={handleSubmit}>
//                 <div className="form-group">
//                     <label>이름:</label>
//                     <input
//                         type="text"
//                         value={name}
//                         onChange={(e) => setName(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>표시 순서:</label>
//                     <input
//                         type="number"
//                         value={displayOrder}
//                         onChange={(e) => setDisplayOrder(e.target.value)}
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>부모 카테고리 선택:</label>
//                     <select
//                         value={parentId || ''}
//                         onChange={(e) => setParentId(e.target.value || null)}
//                     >
//                         <option value="">부모 카테고리 없음</option>
//                         {parentCategories.map((category) => (
//                             <option key={category.id} value={category.id}>
//                                 {category.name}
//                             </option>
//                         ))}
//                     </select>
//                 </div>
//                 <button type="submit" className="submit-button">카테고리 생성</button>
//             </form>
//         </div>
//     );
// };
//
// export default CategoryCreate;

//
// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';
//
// const CategoryCreate = () => {
//     const [name, setName] = useState('');
//     const [displayOrder, setDisplayOrder] = useState('');
//     const [parentId, setParentId] = useState(null);
//     const [imageFile, setImageFile] = useState(null);
//     const [parentCategories, setParentCategories] = useState([]);
//
//     const navigate = useNavigate();
//
//     useEffect(() => {
//         axios.get('http://localhost:8080/api/categories', {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 const parentCategories = response.data.filter(category => category.parentId === null);
//                 setParentCategories(parentCategories);
//             })
//             .catch(error => {
//                 console.error('Error fetching categories:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     }, []);
//
//     const handleSubmit = (e) => {
//         e.preventDefault();
//
//         const formData = new FormData();
//         formData.append('name', name);
//         formData.append('displayOrder', displayOrder ? Number(displayOrder) : null);
//         if (parentId) formData.append('parentId', parentId);
//         if (imageFile) formData.append('imageFile', imageFile);
//
//         axios.post('http://localhost:8080/api/admin/categories', formData, {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`,
//                 'Content-Type': 'multipart/form-data'
//             }
//         })
//             .then(() => {
//                 navigate('/admin/categories');
//             })
//             .catch(error => {
//                 console.error('Error creating category:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     };
//
//     return (
//         <div className="category-create-container">
//             <h2>카테고리 생성</h2>
//             <form onSubmit={handleSubmit}>
//                 <div className="form-group">
//                     <label>이름:</label>
//                     <input
//                         type="text"
//                         value={name}
//                         onChange={(e) => setName(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>표시 순서:</label>
//                     <input
//                         type="number"
//                         value={displayOrder}
//                         onChange={(e) => setDisplayOrder(e.target.value)}
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>부모 카테고리 선택:</label>
//                     <select
//                         value={parentId || ''}
//                         onChange={(e) => setParentId(e.target.value || null)}
//                     >
//                         <option value="">부모 카테고리 없음</option>
//                         {parentCategories.map((category) => (
//                             <option key={category.id} value={category.id}>
//                                 {category.name}
//                             </option>
//                         ))}
//                     </select>
//                 </div>
//                 <div className="form-group">
//                     <label>이미지 파일:</label>
//                     <input
//                         type="file"
//                         onChange={(e) => setImageFile(e.target.files[0])}
//                     />
//                 </div>
//                 <button type="submit" className="submit-button">카테고리 생성</button>
//             </form>
//         </div>
//     );
// };
//
// export default CategoryCreate;

// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';
//
// const CategoryCreate = () => {
//     const [name, setName] = useState('');
//     const [displayOrder, setDisplayOrder] = useState('');
//     const [mainDisplayOrder, setMainDisplayOrder] = useState(''); // 메인 카테고리 정렬
//     const [parentId, setParentId] = useState(null);
//     const [imageFile, setImageFile] = useState(null);
//     const [previewImageUrl, setPreviewImageUrl] = useState(null); // 이미지 미리보기 URL 상태
//     const [parentCategories, setParentCategories] = useState([]);
//
//     const navigate = useNavigate();
//
//     useEffect(() => {
//         axios.get('http://localhost:8080/api/categories', {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => {
//                 const parentCategories = response.data.filter(category => category.parentId === null);
//                 setParentCategories(parentCategories);
//             })
//             .catch(error => {
//                 console.error('Error fetching categories:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     }, []);
//
//     const handleImageChange = (e) => {
//         const file = e.target.files[0];
//         setImageFile(file);
//         setPreviewImageUrl(URL.createObjectURL(file)); // 선택한 파일의 미리보기 URL 설정
//     };
//
//     const handleSubmit = (e) => {
//         e.preventDefault();
//
//         const formData = new FormData();
//         formData.append('name', name);
//         formData.append('displayOrder', displayOrder ? Number(displayOrder) : null);
//         // formData.append('mainDisplayOrder', mainDisplayOrder ? Number(mainDisplayOrder) : null); // 메인 카테고리 정렬
//         formData.append('mainDisplayOrder', mainDisplayOrder === '' ? null : Number(mainDisplayOrder));
//
//         if (parentId) formData.append('parentId', parentId);
//         if (imageFile) formData.append('imageFile', imageFile);
//
//         axios.post('http://localhost:8080/api/admin/categories', formData, {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`,
//                 'Content-Type': 'multipart/form-data'
//             }
//         })
//             .then(() => {
//                 navigate('/admin/categories');
//                 window.location.reload(); // 페이지 새로고침 추가
//             })
//             .catch(error => {
//                 console.error('Error creating category:', error);
//                 if (error.response && error.response.status === 401) {
//                     alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
//                 }
//             });
//     };
//
//     return (
//         <div className="category-create-container">
//             <h2>카테고리 생성</h2>
//             <form onSubmit={handleSubmit}>
//                 <div className="form-group">
//                     <label>이름:</label>
//                     <input
//                         type="text"
//                         value={name}
//                         onChange={(e) => setName(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>표시 순서:</label>
//                     <input
//                         type="number"
//                         value={displayOrder}
//                         onChange={(e) => setDisplayOrder(e.target.value)}
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>메인 카테고리 정렬 순서:</label>
//                     <input
//                         type="number"
//                         value={mainDisplayOrder}
//                         onChange={(e) => setMainDisplayOrder(e.target.value)}
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>부모 카테고리 선택:</label>
//                     <select
//                         value={parentId || ''}
//                         onChange={(e) => setParentId(e.target.value || null)}
//                     >
//                         <option value="">부모 카테고리 없음</option>
//                         {parentCategories.map((category) => (
//                             <option key={category.id} value={category.id}>
//                                 {category.name}
//                             </option>
//                         ))}
//                     </select>
//                 </div>
//                 <div className="form-group">
//                     <label>이미지 파일:</label>
//                     <input
//                         type="file"
//                         accept="image/*"
//                         onChange={handleImageChange}
//                     />
//                     {/* 이미지 미리보기 추가 */}
//                     {previewImageUrl && (
//                         <div className="image-preview">
//                             <img src={previewImageUrl} alt="Preview"
//                                  style={{width: '100px', marginTop: '10px', borderRadius: '8px'}}/>
//                         </div>
//                     )}
//                 </div>
//                 <button type="submit" className="submit-button">카테고리 생성</button>
//             </form>
//         </div>
//     );
// };
//
// export default CategoryCreate;

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const CategoryCreate = () => {
    const [name, setName] = useState('');
    const [displayOrder, setDisplayOrder] = useState('');
    const [mainDisplayOrder, setMainDisplayOrder] = useState('');
    const [parentId, setParentId] = useState(null);
    const [imageFile, setImageFile] = useState(null);
    const [previewImageUrl, setPreviewImageUrl] = useState(null);
    const [parentCategories, setParentCategories] = useState([]);

    const navigate = useNavigate();

    useEffect(() => {
        axios.get('http://localhost:8080/api/categories', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                const parentCategories = response.data.filter(category => category.parentId === null);
                setParentCategories(parentCategories);
            })
            .catch(error => {
                console.error('Error fetching categories:', error);
                if (error.response && error.response.status === 401) {
                    alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
                }
            });
    }, []);

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        setImageFile(file);
        setPreviewImageUrl(URL.createObjectURL(file));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('name', name);
        formData.append('displayOrder', displayOrder ? Number(displayOrder) : null);

        // mainDisplayOrder가 빈칸이 아닐 때만 추가
        if (mainDisplayOrder !== '') {
            formData.append('mainDisplayOrder', Number(mainDisplayOrder));
        }

        if (parentId) formData.append('parentId', parentId);
        if (imageFile) formData.append('imageFile', imageFile);

        axios.post('http://localhost:8080/api/admin/categories', formData, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(() => {
                navigate('/admin/categories');
                window.location.reload();
            })
            .catch(error => {
                console.error('Error creating category:', error);
                if (error.response && error.response.status === 401) {
                    alert("인증이 필요합니다. 로그인 상태를 확인하세요.");
                }
            });
    };

    return (
        <div className="category-create-container">
            <h2>카테고리 생성</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>이름:</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>표시 순서:</label>
                    <input
                        type="number"
                        value={displayOrder}
                        onChange={(e) => setDisplayOrder(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label>메인 카테고리 정렬 순서:</label>
                    <input
                        type="number"
                        value={mainDisplayOrder}
                        onChange={(e) => setMainDisplayOrder(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label>부모 카테고리 선택:</label>
                    <select
                        value={parentId || ''}
                        onChange={(e) => setParentId(e.target.value || null)}
                    >
                        <option value="">부모 카테고리 없음</option>
                        {parentCategories.map((category) => (
                            <option key={category.id} value={category.id}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label>이미지 파일:</label>
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleImageChange}
                    />
                    {previewImageUrl && (
                        <div className="image-preview">
                            <img src={previewImageUrl} alt="Preview"
                                 style={{ width: '100px', marginTop: '10px', borderRadius: '8px' }}
                            />
                        </div>
                    )}
                </div>
                <button type="submit" className="submit-button">카테고리 생성</button>
            </form>
        </div>
    );
};

export default CategoryCreate;

