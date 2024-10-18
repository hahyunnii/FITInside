import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './BannerAdminMain.css';

// const BannerAdminMain = () => {
//     const [banners, setBanners] = useState([]);
//     const navigate = useNavigate();
//
//     useEffect(() => {
//         // 배너 목록 가져오기 (Authorization 헤더 추가)
//         axios.get('http://localhost:8080/api/banners', {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(response => setBanners(response.data))
//             .catch(error => console.error('Error fetching banners:', error));
//     }, []);
//
//     const handleCreateBanner = () => {
//         navigate('/admin/banner/create');
//     };
//
//     const handleUpdateBanner = (id) => {
//         navigate(`/admin/banner/update/${id}`);
//     };
//
//     const handleDeleteBanner = (id) => {
//         // 배너 삭제 요청 (Authorization 헤더 추가)
//         axios.delete(`http://localhost:8080/api/admin/banners/${id}`, {
//             headers: {
//                 'Authorization': `Bearer ${localStorage.getItem('token')}`
//             }
//         })
//             .then(() => setBanners(banners.filter(banner => banner.id !== id)))
//             .catch(error => console.error('Error deleting banner:', error));
//     };
//
//     return (
//         <div>
//             <h1>Banner Management</h1>
//             <button onClick={handleCreateBanner}>Create Banner</button>
//
//             <table className="banner-table">
//                 <thead>
//                 <tr>
//                     <th>ID</th>
//                     <th>Image</th>
//                     <th>Title</th>
//                     <th>Display Order</th>
//                     <th>Actions</th>
//                 </tr>
//                 </thead>
//                 <tbody>
//                 {banners.map(banner => (
//                     <tr key={banner.id}>
//                         <td>{banner.id}</td>
//                         <td>
//                             <img src={banner.imageUrl} alt={banner.title} width="100" />
//                         </td>
//                         <td>{banner.title}</td>
//                         <td>{banner.displayOrder}</td>
//                         <td>
//                             <button onClick={() => handleUpdateBanner(banner.id)}>Edit</button>
//                             <button onClick={() => handleDeleteBanner(banner.id)}>Delete</button>
//                         </td>
//                     </tr>
//                 ))}
//                 </tbody>
//             </table>
//         </div>
//     );
// };
//
// export default BannerAdminMain;

const BannerAdminMain = () => {
    const [banners, setBanners] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // 배너 목록 가져오기 (Authorization 헤더 추가)
        axios.get('http://localhost:8080/api/banners', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(response => {
                // 배너를 displayOrder 기준으로 정렬
                const sortedBanners = response.data.sort((a, b) => a.displayOrder - b.displayOrder);
                setBanners(sortedBanners);
            })
            .catch(error => console.error('Error fetching banners:', error));
    }, []);

    const handleCreateBanner = () => {
        navigate('/admin/banner/create');
    };

    const handleUpdateBanner = (id) => {
        navigate(`/admin/banner/update/${id}`);
    };

    const handleDeleteBanner = (id) => {
        // 배너 삭제 요청 (Authorization 헤더 추가)
        axios.delete(`http://localhost:8080/api/admin/banners/${id}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(() => setBanners(banners.filter(banner => banner.id !== id)))
            .catch(error => console.error('Error deleting banner:', error));
        window.location.reload(); // 새로고침
        alert("카테고리가 삭제되었습니다.");
    };

    return (
        <div>
            <h1>Banner Management</h1>
            <button onClick={handleCreateBanner}>Create Banner</button>

            <table className="banner-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Image</th>
                    <th>Title</th>
                    <th>Display Order</th>
                    <th>Edit</th>
                    <th>Delete</th>
                </tr>
                </thead>
                <tbody>
                {banners.map(banner => (
                    <tr key={banner.id}>
                        <td>{banner.id}</td>
                        <td>
                            <img src={banner.imageUrl} alt={banner.title} width="100" />
                        </td>
                        <td>{banner.title}</td>
                        <td>{banner.displayOrder}</td>
                        <td>
                            <button onClick={() => handleUpdateBanner(banner.id)}>Edit</button>
                        </td>
                        <td>
                            <button onClick={() => handleDeleteBanner(banner.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default BannerAdminMain;
