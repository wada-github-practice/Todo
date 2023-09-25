<template>
	<div class="container">
		<div class="row">
			<div class="col">
				<input type="text" maxlength="40" v-model="title" class="form-control"/>
			</div>
			<div class="col">
				<input type="text" maxlength="40" v-model="content" class="form-control"/>
			</div>
			<div class="col">
				<input type="text" maxlength="40" v-model="category" class="form-control"/>
			</div>
			<div class="col">
				<input type="date" maxlength="40" v-model="deadline" class="form-control"/>
			</div>
			<div class="col">
				<button class="btn btn-primary" v-on:click="createTodo">新しくtodoを追加する</button>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="col">
				<label class="form-label">Todolistをエクセルに保存する！</label>
				<button type="button" @click="onExport(type)" class="form-control">出力</button>
			</div>
			<div class="col">
				<label class="form-label">タイトルで検索！</label>
				<input type="text" v-model="keywordTitle" class="form-control">
			</div>
			<div class="col">
				<label class="form-label">内容で検索！</label>
				<input type="text" v-model="keywordContent" class="form-control">
			</div>
			<div class="col">
				<label class="form-label">カテゴリで検索！</label>
				<input type="text" v-model="keywordCategory" class="form-control">
			</div>
		</div>
	</div>
	<div class="col">
				<label class="form-label">工事中</label>
				<input type="file" @change="onChange" ref="upload"/>
			</div>
	<div>
		Todoリストは{{countTodos()}}件あります。
	</div>
	<label class="form-label">todo</label>
	<div class="container">
		<table class="table table-striped" ref="table-to-sheet">
			<tr>
				<th>タイトル:</th>
				<th>内容:</th>
				<th>カテゴリ:</th>
				<th>締め切り:</th>
				<th>状態:</th>
				<th></th>
				<th></th>
				<th></th>
			</tr>
			<tbody>
				<tr v-for="todo in filteredTodos" v-bind:key="todo.id">
					<td v-if="!editList.includes(todo)">{{ todo.title }}</td>
					<td v-if="editList.includes(todo)"><input v-if="editList.includes(todo)" maxlength="40" v-model="todo.title" class="form-control"/></td>
					<td v-if="!editList.includes(todo)">{{ todo.content }}</td>
					<td v-if="editList.includes(todo)"><input v-if="editList.includes(todo)" maxlength="40" v-model="todo.content" class="form-control"/></td>
					<td v-if="!editList.includes(todo)">{{ todo.category }}</td>
					<td v-if="editList.includes(todo)"><input v-if="editList.includes(todo)" maxlength="40" v-model="todo.category" class="form-control"/></td>
					<td v-if="!editList.includes(todo)">{{ todo.deadline }}</td>
					<td v-if="editList.includes(todo)"><input type="date" v-if="editList.includes(todo)" maxlength="40" v-model="todo.deadline" class="form-control"/></td>
					<td v-if="editList.includes(todo)"></td>
					<td v-if="editList.includes(todo)"></td>
					<td v-if="editList.includes(todo)"><button @click="doneEdit(todo.id)" v-if="editList.includes(todo)" class="btn btn-warning">確定</button></td>
					<td v-if="!editList.includes(todo)">{{ todo.state }}</td>
					<td v-if="!editList.includes(todo)"><button v-on:click="deleteTodo(todo.id)"  class="btn btn-primary">削除</button></td>
					<td v-if="!editList.includes(todo)"><button v-on:click="updateTodo(todo.id)"  class="btn btn-warning">編集</button></td>
					<td><button v-on:click="finishTodo(todo.id)" v-if="todo.state=='Ready'" class="btn btn-info">Done!</button></td>
				</tr>
			</tbody>
		</table>
	</div>
</template>
<script>
import axios from 'axios'
import {saveAs} from "file-saver";
// readfileAsyncを呼び出すため
import ExcelJS from "exceljs" ;
export default {
  data () {
    return {
	  keywordContent: '',
	  keywordTitle: '',
	  keywordCategory: '',
      todoList: [],
	  editList: [],
	  TodoListCount: [],
	  id:'',
      title: 'title',
      content: 'content',
	  category: 'category',
	  deadline: '',
      username: 'wada',
	  editId:'',
	  type: "xlsx",
    }
  },
  methods:{
	createTodo: function () {
		const todo = {
			title: this.title,
			content: this.content,
			category: this.category,
			deadline: this.deadline,
			username: this.username
		};
        axios
          .post('/todo/insert/' + this.username,todo)
          .then((response) =>(this.todoList.push(response.data)))
          .catch((err) => {
            console.log(err);
          });
      },
	showTodo: function(){
		axios
		.get('todo/list/' + this.username)
		.then(response => (this.todoList = response.data));
	},
    deleteTodo: function(id2){
		axios
		  .post('/todo/delete/' + id2)
          .then((response) =>{ const index = this.todoList.findIndex((v)=>v.id===id2);this.todoList.splice(index,1)})
          .catch((err) => {
            console.log(err);
          });
	},
	finishTodo: function(id3){
		axios
		  .post('/todo/finish/' + id3)
          .then((response) =>{ const index = this.todoList.findIndex((v)=>v.id===id3);this.todoList[index].state = 'Finish'})
          .catch((err) => {
            console.log(err);
          });
	},
	updateTodo: function(id4){
		const index = this.todoList.findIndex((v)=>v.id===id4);
		const editTodo = Object.assign({}, this.todoList[index]);
		this.editList.push(this.todoList[index]);
		this.editId = index;
	},
	doneEdit: async function(id5){
		const index = this.editList.findIndex((v)=>v.id===id5);
		const todo = this.editList[index];
		console.log(todo);
		await axios
		  .post('/todo/edit/' + id5,todo)
          .then((response) =>{this.todoList})
          .catch((err) => {
            console.log(err);
          });
		  this.editList.splice(index,1);
	},
	countTodos: function(){
		return this.TodoListCount.length
	},
	async onExport(ext) {
		const workbook = new ExcelJS.Workbook(); // workbookを作成
		workbook.addWorksheet("Todo"); // worksheetを追加
		const worksheet = workbook.getWorksheet("Todo"); // 追加したworksheetを取得

		// 各列のヘッダー
		worksheet.columns = [
			{ header: "タスクid", key: "id" },
			{ header: "タイトル", key: "title" },
			{ header: "内容", key: "content" },
			{ header: "カテゴリ", key: "category" },
			{ header: "締め切り", key: "deadline" },
			{ header: "状態", key: "state" },
		];
      // 各行のデータ（worksheet.columnsのkeyがオブジェクトのキーと同じになる）
		for(var i in this.todoList) {
			var todo = this.todoList[i];
			if(todo.content.indexOf(this.keywordContent) !== -1 &&
				todo.title.indexOf(this.keywordTitle) !== -1 && todo.category.indexOf(this.keywordCategory) !== -1){
					worksheet.addRows([
						todo
				])
			}
		}
		var now = new Date();
		var res = "" + now.getFullYear() + "/" + (now.getMonth() + 1) + 
			"/" + (now.getDate()) + " " + (now.getHours()) + ":" + 
			(now.getMinutes()) + ":" + (now.getSeconds());

		const uint8Array =
			ext === "xlsx"
			? await workbook.xlsx.writeBuffer() // xlsxの場合
			: await workbook.csv.writeBuffer(); // csvの場合
		const blob = new Blob([uint8Array], { type: "application/octet-binary" });
		const a = document.createElement("a");
		a.href = (window.URL || window.webkitURL).createObjectURL(blob);
		a.download = `Todo_${res}.${ext}`;
		a.click();
		a.remove();
    },
  },
  computed:{
    filteredTodos: function() {
			this.TodoListCount.splice(0);
			var todoList = [];
			for(var i in this.todoList) {
				var todo = this.todoList[i];
				if(todo.content.indexOf(this.keywordContent) !== -1 &&
					todo.title.indexOf(this.keywordTitle) !== -1 && todo.category.indexOf(this.keywordCategory) !== -1) {
						todoList.push(todo);
						this.TodoListCount.push(todo);
			}
		}
		return todoList;
	}
  },
  	created(){
		axios
		.get('todo/list/' + this.username)
		.then(response => {this.todoList = response.data})
		.catch((err) => {console.log(err);});
  	},
	mounted() {
		
	},
}
</script>
