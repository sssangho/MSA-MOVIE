let reviewModal;
let movieMap = {};   // 영화 ID → 제목 매핑
let allReviews = []; // 전체 리뷰 캐시

document.addEventListener('DOMContentLoaded', async function() {
    reviewModal = new bootstrap.Modal(document.getElementById('reviewModal'));
    await loadMovies(); // 🎬 영화 목록 먼저
    await loadReviews(); // 💬 리뷰 목록
});

// 🎬 영화 목록 불러오기
async function loadMovies() {
    try {
        const response = await fetch('/api/movies');
        const movies = await response.json();

        movieMap = {};
        const movieSelect = document.getElementById('movieId');
        const movieFilter = document.getElementById('movieFilter');

        movieSelect.innerHTML = '<option value="">영화를 선택하세요</option>';
        movieFilter.innerHTML = '<option value="">🎬 전체 영화 보기</option>';

        movies.forEach(movie => {
            movieMap[String(movie.id)] = movie.title;

            // 등록 모달용
            const option1 = document.createElement('option');
            option1.value = movie.id;
            option1.textContent = movie.title;
            movieSelect.appendChild(option1);

            // 필터용
            const option2 = document.createElement('option');
            option2.value = movie.id;
            option2.textContent = movie.title;
            movieFilter.appendChild(option2);
        });
    } catch (error) {
        console.error("영화 목록 불러오기 실패:", error);
    }
}

// ⭐ 평점을 별(⭐)로 변환
function renderStars(rating) {
    rating = Number(rating);
    if (isNaN(rating)) rating = 0;
    rating = Math.max(0, Math.min(rating, 5));
    return '⭐'.repeat(rating) + '☆'.repeat(5 - rating);
}

// 💬 리뷰 목록 불러오기
async function loadReviews() {
    try {
        const response = await fetch('/api/reviews');
        const reviews = await response.json();

        allReviews = reviews; // ✅ 캐시 저장
        renderReviewTable(reviews);
    } catch (error) {
        console.error('리뷰 목록을 불러오는데 실패했습니다:', error);
    }
}

// 🎨 리뷰 테이블 렌더링
function renderReviewTable(reviews) {
    const tbody = document.getElementById('reviewTableBody');
    tbody.innerHTML = '';

    reviews.forEach(review => {
        const movieTitle = movieMap[String(review.movieId)] || `#${review.movieId}`;
        const reviewerName = review.reviewerName || '익명'; // ✅ 기본값 처리

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${review.id}</td>
            <td>${movieTitle}</td>
            <td>${reviewerName}</td> <!-- ✅ 리뷰어 추가 -->
            <td>${review.comment}</td>
            <td>${renderStars(review.rating)}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="editReview(${review.id})">수정</button>
                <button class="btn btn-sm btn-danger" onclick="deleteReview(${review.id})">삭제</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// 🎬 영화 필터링 기능
function filterReviews() {
    const selectedMovieId = document.getElementById('movieFilter').value;
    if (!selectedMovieId) {
        renderReviewTable(allReviews); // 전체 보기
    } else {
        const filtered = allReviews.filter(r => String(r.movieId) === String(selectedMovieId));
        renderReviewTable(filtered);
    }
}

// ➕ 리뷰 추가 모달
function showAddReviewModal() {
    document.getElementById('modalTitle').textContent = '리뷰 추가';
    document.getElementById('reviewForm').reset();
    document.getElementById('reviewId').value = '';
    reviewModal.show();
}

// ✏️ 리뷰 수정
async function editReview(id) {
    try {
        const response = await fetch(`/api/reviews/${id}`);
        const review = await response.json();

        document.getElementById('modalTitle').textContent = '리뷰 수정';
        document.getElementById('reviewId').value = review.id;
        document.getElementById('movieId').value = review.movieId; // ✅ 자동 선택
        document.getElementById('reviewerName').value = review.reviewerName || ''; // ✅ 리뷰어 추가
        document.getElementById('comment').value = review.comment;
        document.getElementById('rating').value = review.rating;

        reviewModal.show();
    } catch (error) {
        console.error('리뷰 정보를 불러오는데 실패했습니다:', error);
        alert('리뷰 정보를 불러오는데 실패했습니다.');
    }
}

// 💾 리뷰 저장
async function saveReview() {
    const id = document.getElementById('reviewId').value;
    const movieId = document.getElementById('movieId').value.trim();
    const reviewerName = document.getElementById('reviewerName').value.trim(); // ✅ 리뷰어 추가
    const comment = document.getElementById('comment').value.trim();
    const rating = parseInt(document.getElementById('rating').value);

    if (!movieId || isNaN(movieId)) return alert('⚠️ 영화를 선택하세요.');
    if (!reviewerName) return alert('⚠️ 리뷰 작성자 이름을 입력하세요.');
    if (!comment) return alert('⚠️ 댓글을 입력하세요.');
    if (isNaN(rating) || rating < 1 || rating > 5) return alert('⚠️ 평점은 1~5 사이여야 합니다.');

    const review = { movieId: parseInt(movieId), reviewerName, comment, rating }; // ✅ reviewerName 포함

    try {
        const url = id ? `/api/reviews/${id}` : '/api/reviews';
        const method = id ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(review)
        });

        if (!response.ok) throw new Error('저장 실패');
        reviewModal.hide();
        await loadReviews();
        alert('저장되었습니다.');
    } catch (error) {
        console.error('저장 중 오류:', error);
        alert('저장에 실패했습니다.');
    }
}

// ❌ 리뷰 삭제
async function deleteReview(id) {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    try {
        const response = await fetch(`/api/reviews/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('삭제 실패');
        await loadReviews();
        alert('삭제되었습니다.');
    } catch (error) {
        console.error('삭제 오류:', error);
        alert('삭제에 실패했습니다.');
    }
}
