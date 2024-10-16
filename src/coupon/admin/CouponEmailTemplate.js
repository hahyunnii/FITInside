
const CouponEmailTemplate = ({ coupon }) => {
    // 미리 정의된 이메일 템플릿
    const emailTemplate = `
        <div style="width: 100%; margin: 3em auto; font-family: sans-serif;">
            <h1 style="color: #4CAF50;">쿠폰 선물이 도착했어요~ 🎁🎁</h1>
            <p>안녕하세요! FITinside 입니다!</p>
            <p>이 이메일은 회원님께 쿠폰을 전송하기 위한 이메일입니다. 아래의 쿠폰 코드를 사용하여 할인 혜택을 누리세요.</p>
            
            <div style="margin: 10px 0; display: flex; width: 27em;">
                <div style="height: 13em; padding: 1em; color: #424040; border-radius: 8px; 
                    background: ${coupon.type === 'AMOUNT'
        ? 'linear-gradient(to bottom, #fa3e3e 0%, #fa3e3e 26%, #e3e3e3 26%, #e3e3e3 100%)'
        : 'linear-gradient(to bottom, #3e4efa 0%, #3e4efa 26%, #e3e3e3 26%, #e3e3e3 100%)'}; 
                    flex: 1; position: relative;">
                    <h1 style="color: white;">${coupon.name}
                    </h1>
                    <div style="margin-top: 1em; margin-bottom: 0.5em;">
                        <strong>${coupon.code}</strong>
                    </div>
                    <div style="margin-bottom: 0.5em;">
                        <strong>${coupon.categoryName}</strong>
                    </div>
                    <div style="margin-bottom: 0.5em;">
                        (${coupon.type === 'AMOUNT' ? '고정 금액' : '퍼센티지'})&nbsp;
                        <strong>${coupon.type === 'AMOUNT'
        ? `${new Intl.NumberFormat().format(coupon.value)}원`
        : `${coupon.percentage}%`}</strong> 할인
                    </div>
                    <div style="margin-bottom: 0.5em;">
                        ${coupon.minValue === 0
        ? '최소 주문 금액 없음'
        : new Intl.NumberFormat().format(coupon.minValue) + ' 원 부터 적용 가능'}
                    </div>
                    <div style="margin-bottom: 0.5em;">
                        ~ ${coupon.expiredAt} 까지 적용 가능
                    </div>
                </div>

                <div style="flex: 0 0 6.5em; border-left: .18em dashed #fff; border-radius: 8px; background: #e3e3e3; display: flex; flex-direction: column;">
                    <div style="height: 3.9em; background: ${coupon.type === 'AMOUNT' ? '#fa3e3e' : '#3e4efa'}; border-top-right-radius: 8px; border-bottom-right-radius: 0;">
                        <!-- 오른쪽 상단 부분 -->
                    </div>
                    <div style="flex: 1; background: #e3e3e3; border-bottom-right-radius: 8px;">
                        <!-- 오른쪽 하단 부분 -->
                    </div>
                </div>
            </div>
            
            <p>감사합니다.<br />FITinside 팀</p>
        </div>
    `;

    return emailTemplate; // HTML 문자열을 반환
};

export default CouponEmailTemplate;
