const main = {
	init : function(){
		const _this = this;
		
		/* 게시물 저장 */
		$('#btn-save').on('click', function(){
			_this.save();
		});
		
		/* 게시물 수정 */
		$('#btn-update').on('click', function(){
			_this.update();
		})
		
		/* 게시물 삭제 */
		$('#btn-delete').on('click', function(){
			_this.delete();
		});
		
		/* 댓글 저장 */
		$('#btn-comment-save').on('click', function(){
			_this.commentSave();
		}); 
		
		/* 댓글 삭제 */
		$('#btn-comment-delete').on('click', function(){
			_this.commentDelete();
		});
		
		/* 댓글 수정 */
		document.querySelectorAll('#btn-comment-update').forEach(function (item){
			item.addEventListener('click', function(){ // 버튼 클릭 이벤트 발생 시
				const form = this.closest('form'); // btn의 가장 가까운 조상의 Element(form)을 반환 (closest) 하여 form 변수에 저장
				_this.commentUpdate(form); // 해당 폼 업데이트 수행
			});
		});
	},
	
	/* 글 작성 */
	save : function(){
		const data = {
			title : $('#title').val(),
			content : $('#content').val(),
			writer : $('#writer').val()
		};
		const confirmCheck = confirm("작성하시겠습니까?");
		
		if(confirmCheck === true){
			
			/* 공백과 빈 문자열 체크 */
			if (!data.title || data.title.trim() === "" || !data.content || data.content.trim() === ""){
				alert("공백 또는 입력하지 않은 부분이 존재합니다.");
				return false;
			} else {
				$.ajax({
					type: 'POST',
					url: '/api/post',
					dataType: 'JSON',
					contentType: 'application/json; charset=utf-8',
					data: JSON.stringify(data)
				}).done(function(){
					alert("등록되었습니다.");
					window.location.href="/";
				}).fail(function(error){
					alert(JSON.stringify(error));
				});
			}
		}

	},
	
	/* 글 수정 */
	update : function(){
		const data = {
			id : $('#id').val(),
			title : $('#title').val(),
			content : $('#content').val()
		};
		
		const confirmCheck = confirm("수정하시겠습니까?");
		if(confirmCheck === true){
			
			/* 공백 또는 빈 문자열 확인 */
			if(!data.title || data.title.trim() === "" || !data.content || data.content.trim() === ""){
				alert("공백 또는 입력하지 않은 부분이 존재합니다.");
				return false;
			} else {
				$.ajax({
					type: 'PUT',
					url: '/api/post/'+data.id,
					dataType: 'JSON',
					contentType: 'application/json; charset=utf-8',
					data: JSON.stringify(data)
				}).done(function() {
					alert("수정되었습니다.");
					window.location.href="/post/read/"+data.id;
				}).fail(function (error){
					alert(JSON.stringify(error));
				});
			}
		}
	},
	
	/* 글 삭제 */
	delete : function() {
		const id = $('#id').val();
		
		const confirmCheck = confirm("정말 삭제하시겠습니까?");
		if(confirmCheck === true){
			$.ajax({
				type: 'DELETE',
				url: '/api/post'+id,
				dataType: 'JSON',
				contentType: 'application/json; charset=utf-8'
			}).done(function(){
				alert("삭제되었습니다.");
				window.location.href="/";
			}).fail(function(error){
				alert(JSON.stringify(error));
			});
		}
	},
	
	/* 댓글 부분 */
	
	/* 댓글 저장 */
	commentSave : function(){
		const data = {
			postId: $('#postId').val(),
			content: $('#commentContent').val()
		}
		
		// 공백 및 빈 문자열 체크
		if(!data.content || data.content.trim() === ""){
			alert("공백 또는 입력하지 않은 부분이 있습니다.");
			return false;
		} else {
			$.ajax({
				type: 'POST',
				url: '/api/post/'+data.postId + '/comment',
				contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(data)
			}).done(function(){
				alert("댓글이 등록되었습니다.");
				window.location.reload();
			}).fail(function(error){
				alert(JSON.stringify(error));
			});
		}
	},
	
	/* 댓글 수정 */
	commentUpdate : function(form){
		const data = {
			id: form.querySelector('#id').value,
			postId: form.querySelector('#postId').value,
			content: form.querySelector('#comment-content').value,
			writerUserId: form.querySelector('#writerUserId').value,
			sessionUserId: form.querySelector('#sessionUserId').value
		}
		
		/* 댓글 작성자와 현재 로그인한 사용자 확인 */
		console.log("commentWriterId : "+data.writerUserId);
		console.log("sessionUserId : "+data.sessionUserId);
		
		if(data.writerUserId !== data.sessionUserId){
			alert("본인이 작성한 댓글만 수정할 수 있습니다.");
			return false;
		}
		
		/* 공백 또는 빈 문자열 검사 */
		if(!data.content || data.content.trim() === ""){
			alert("공백 또는 입력하지 않은 부분이 있습니다.");
			return false;
		}
		
		const confirmCheck = confirm("수정하시겠습니까?");
		if(confirmCheck){
			$.ajax({
				type: 'PUT',
				url: '/api/post/'+data.postId+"/comment/"+data.id,
				contentType: 'application/json; charset=utf-8',
				data: JSON.stringify(data)
			}).done(function(){
				window.location.reload();
			}).fail(function(error){
				alert(JSON.stringify(error));
			});
		}
	},
	
	/* 댓글 삭제 */
	commentDelete : function(postId, commentId, commentWriterId, sessionUserId){
		
		// 본인이 작성한 글인지 확인
		if(commentWriterId !== sessionUserId){
			alert("본인이 작성한 댓글만 삭제할 수 있습니다.");
		} else {
			con_check = confirm("삭제하시겠습니까?");
		
			if(con_check === true){
				$.ajax({
					type: 'DELETE',
					url: '/api/post/'+postId+'/comment/'+commentId,
					dataType: 'JSON'
				}).done(function(){
					alert('댓글이 삭제되었습니다.');
					window.location.reload();
				}).fail(function(error){
					alert(JSON.stringfy(error));
				});
			}
		}

	}
};

main.init();