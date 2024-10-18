// import React, { useState } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';
//
// const BannerCreate = () => {
//     const [title, setTitle] = useState('');
//     const [displayOrder, setDisplayOrder] = useState(0);
//     const [image, setImage] = useState(null);
//     const navigate = useNavigate();
//
//     const handleSubmit = async (e) => {
//         e.preventDefault();
//         const formData = new FormData();
//         formData.append('title', title);
//         formData.append('displayOrder', displayOrder);
//         formData.append('image', image);
//
//         try {
//             await axios.post('http://localhost:8080/api/admin/banners', formData, {
//                 headers: {
//                     'Content-Type': 'multipart/form-data',
//                     'Authorization': `Bearer ${localStorage.getItem('token')}` // Authorization 헤더 추가
//                 }
//             });
//             navigate('/admin/banners');
//         } catch (error) {
//             console.error('Error creating banner:', error);
//         }
//     };
//
//     return (
//         <div>
//             <h1>Create New Banner</h1>
//             <form onSubmit={handleSubmit}>
//                 <div>
//                     <label>Title</label>
//                     <input
//                         type="text"
//                         value={title}
//                         onChange={(e) => setTitle(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div>
//                     <label>Display Order</label>
//                     <input
//                         type="number"
//                         value={displayOrder}
//                         onChange={(e) => setDisplayOrder(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div>
//                     <label>Image</label>
//                     <input
//                         type="file"
//                         onChange={(e) => setImage(e.target.files[0])}
//                         required
//                     />
//                 </div>
//                 <button type="submit">Create Banner</button>
//             </form>
//         </div>
//     );
// };
//
// export default BannerCreate;


// import React, { useState } from 'react';
// import axios from 'axios';
// import { useNavigate } from 'react-router-dom';
// import './BannerCreate.css'; // CSS 파일 추가
//
// const BannerCreate = () => {
//     const [title, setTitle] = useState('');
//     const [displayOrder, setDisplayOrder] = useState(0);
//     const [image, setImage] = useState(null);
//     const navigate = useNavigate();
//
//     const handleSubmit = async (e) => {
//         e.preventDefault();
//         const formData = new FormData();
//         formData.append('title', title);
//         formData.append('displayOrder', displayOrder);
//         formData.append('image', image);
//
//         try {
//             await axios.post('http://localhost:8080/api/admin/banners', formData, {
//                 headers: {
//                     'Content-Type': 'multipart/form-data',
//                     'Authorization': `Bearer ${localStorage.getItem('token')}`
//                 }
//             });
//             navigate('/admin/banners');
//         } catch (error) {
//             console.error('Error creating banner:', error);
//         }
//     };
//
//     return (
//         <div className="form-container">
//             <h1>Create New Banner</h1>
//             <form onSubmit={handleSubmit}>
//                 <div className="form-group">
//                     <label>Title</label>
//                     <input
//                         type="text"
//                         value={title}
//                         onChange={(e) => setTitle(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>Display Order</label>
//                     <input
//                         type="number"
//                         value={displayOrder}
//                         onChange={(e) => setDisplayOrder(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>Image</label>
//                     <input
//                         type="file"
//                         onChange={(e) => setImage(e.target.files[0])}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <button type="submit">Create Banner</button>
//                 </div>
//             </form>
//         </div>
//     );
// };
//
// export default BannerCreate;

import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './BannerCreate.css'; // CSS 파일 추가

const BannerCreate = () => {
    const [title, setTitle] = useState('');
    const [displayOrder, setDisplayOrder] = useState(0);
    const [image, setImage] = useState(null);
    const [targetUrl, setTargetUrl] = useState(''); // targetUrl 필드 추가
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('title', title);
        formData.append('displayOrder', displayOrder);
        formData.append('image', image);

        // targetUrl이 입력되었을 때만 추가
        if (targetUrl) {
            formData.append('targetUrl', targetUrl);
        }

        try {
            await axios.post('http://localhost:8080/api/admin/banners', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            navigate('/admin/banners');
        } catch (error) {
            console.error('Error creating banner:', error);
        }
    };

    return (
        <div className="form-container">
            <h1>Create New Banner</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Title</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Display Order</label>
                    <input
                        type="number"
                        value={displayOrder}
                        onChange={(e) => setDisplayOrder(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Image</label>
                    <input
                        type="file"
                        onChange={(e) => setImage(e.target.files[0])}
                        required
                    />
                </div>
                <div className="form-group"> {/* targetUrl 입력 필드, 필수가 아님 */}
                    <label>Target URL (Optional)</label>
                    <input
                        type="text"
                        value={targetUrl}
                        onChange={(e) => setTargetUrl(e.target.value)}
                        placeholder="http://example.com"
                    />
                </div>
                <div className="form-group">
                    <button type="submit">Create Banner</button>
                </div>
            </form>
        </div>
    );
};

export default BannerCreate;

