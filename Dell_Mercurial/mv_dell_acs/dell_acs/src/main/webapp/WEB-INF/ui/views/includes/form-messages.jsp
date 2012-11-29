<div>
    <c:choose>
        <c:when test="${ ! empty error}">
            <div id="error-box" class="error-box" style="background-color:#ffe4e1;">
                <div>
                    <span class="error-icon"></span>
                        ${error}
                </div>
            </div>
        </c:when>
    </c:choose>
</div>
