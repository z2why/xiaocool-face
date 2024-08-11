<template>


  <div style="padding: 10px">
    <div id="biaotou">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="font-size: larger">
        <el-breadcrumb-item :to="{ path: '/' }"><i class="el-icon-s-home"></i>首页</el-breadcrumb-item>
        <el-breadcrumb-item>文件下载</el-breadcrumb-item>
      </el-breadcrumb>
      <el-divider></el-divider>
    </div>

    <div style="margin: 10px 0;display: flex;flex-direction: row;justify-content: space-between;align-items: center;">
      <div>
        <el-upload
            action="string"
            ref="upload"
            :show-file-list="false"
            :http-request="UploadSectionFile"
            style="display: inline-block">
          <el-button type="primary" class="ml-5">上传文件 <i class="el-icon-top"></i></el-button>
        </el-upload>
        <el-popconfirm
            class="ml-5"
            confirm-button-text='确定'
            cancel-button-text='我再想想'
            icon="el-icon-info"
            icon-color="red"
            title="您确定批量删除这些数据吗？"
            @confirm="delBatch"
        >
          <el-button type="danger" slot="reference" style="position: relative;left: 10px;" :disabled="delBtlStatu">批量删除
            <i class="el-icon-remove-outline"></i></el-button>
        </el-popconfirm>
      </div>

      <div>
        <el-input style="width: 200px;margin-right: 10px;" placeholder="请输入名称" suffix-icon="el-icon-search"
                  v-model="folder"></el-input>
        <el-button class="ml-5" type="primary" @click="navigateToName(folder)" style="">搜索</el-button>
        <el-button type="warning" @click="reset" style="">重置</el-button>
      </div>
    </div>

    <div>
      <el-card>
        <el-row>
          <el-col :span="5">
            <el-tree
                class="el-tree"
                :data="treeData"
                :props="treeProps"
                :default-expanded-keys="defaultExpanded"
                @node-contextmenu="rightClick"
                node-key="id"
                highlight-current
                @node-click="onTreeNodeClick"
                ref="tree"
                @node-expand="NodeExpandOpen"
                @node-collapse="NodeExpandClose"
            >
             <span class="custom-tree-node" slot-scope="{ node, data }">
               <div class="tree-node-l">
               <img :src="NodeImg(data)" class="el-treeSvg"/>
               <span v-if="!data.isAddFolder" :title="node.label" v-text="node.label"></span>
               </div>

               <input style="max-width: 120px;width: calc(100% - 65px);" v-if="data.isAddFolder" placeholder="请输入名称" v-model="folderName"/>


                <div v-if="data.isAddFolder" class="btn-r">
                <el-button @click="appendFolder()" type="text" icon="el-icon-check"></el-button>
                <el-button style="margin-left: 5px" @click="canceladFolder()" type="text" icon="el-icon-close"></el-button>
              </div>
             </span>

            </el-tree>

          </el-col>

          <el-col :span="19">
            <el-breadcrumb separator-class="el-icon-arrow-right"
                           style="margin-left: 15px;height: 30px;font-size: medium">
              <el-breadcrumb-item
                  v-for="(item, index) in breadcrumb"
                  :key="index"
                  @click.native="onBreadcrumbClick(index)"
              >
                <a>{{ item }}</a>
              </el-breadcrumb-item>
            </el-breadcrumb>
            <el-table  v-loading="loading" element-loading-text="拼命加载中"
                :data="tableData" border stripe :header-cell-class-name="'headerBg'" class="el-tree"
                      @selection-change="handleSelectionChange"
                      @row-dblclick="onRowDblClick"
            >
              <el-table-column label="文件名称">
                <template #default="{ row }">
              <span style="display: flex;align-items: center;cursor: pointer">
               <img style="height: 20px;margin-right: 10px" :src='dynamicImageUrl(row.type,row.isFile)'>
                {{ row.name }}
               </span>
                </template>
              </el-table-column>
              <el-table-column prop="size" label="大小(KB)" width="150" align="center"/>
              <el-table-column label="操作" width="250" align="center">
                <template slot-scope="scope">
                  <el-button v-if="scope.row.isFile" style="margin-right: 10px" type="primary" slot="reference"
                             @click="download(scope.row)">
                    下载<i class="el-icon-download"></i></el-button>
                  <el-popconfirm
                      class="ml-5"
                      confirm-button-text='确定'
                      cancel-button-text='我再想想'
                      icon="el-icon-info"
                      icon-color="red"
                      title="您确定删除吗？"
                      @confirm="del(scope.row)"
                  >
                    <el-button type="danger" slot="reference" icon="el-icon-remove-outline">
                      {{ scope.row.isFile ? '删除' : '删除文件夹' }}
                    </el-button>
                  </el-popconfirm>
                </template>
              </el-table-column>

            </el-table>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <div v-show="menuVisible">
      <ul id="menu" class="menu" :style="clickNode.isFile?'height:70px;width:100px':''">
        <li class="menu__item" v-if="!clickNode.isFile" @click="appendNode(clickNode)">添加文件夹</li>
        <li class="menu__item" v-if="!clickNode.isFile" @click="addCard(clickNode)">上传文件</li>
        <li class="menu__item" v-if="clickNode.isFile" @click="download(clickNode)">下载文件</li>
        <li class="menu__item" @click="del(clickNode)">删 除</li>
      </ul>
    </div>


  </div>
</template>

<script>
export default {
  name: "File",
  data() {
    return {
      menuVisible: false,
      name: '',
      qeq: [],
      multipleSelection: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      delBtlStatu: true,
      nextMarker: '',
      treeData: [], // 树形结构数据
      tableData: [], // 表格数据
      currentPath: [], // 当前路径
      breadcrumb: [], // 面包屑数据
      loading:true,
      treeProps: {
        label: 'name',
        children: 'children',
        type: 'type',
        isOpen: 'isOpen',
        id: 'id'
      },
      folder: '',
      path: '',
      uploadfolder: '',
      clickNode:'',
      defaultExpanded:[1],
      folderName:''

    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/service/cos/list", {
        params: {
          folder: '',
          nextMarker: this.nextMarker
        }
      }).then(res => {
        if (res.code === "200") {
          if (res.data.nextMarker) {
            this.nextMarker = res.data.nextMarker
          }
          this.loading=false
          this.treeData = [res.data.list];
          this.treeData = this.treeData[0].children
          this.updateTableData(this.treeData[0]);
          this.treeData[0].isOpen = true
          this.updateBreadcrumb(this.treeData[0].path)

        } else {
          this.$message.error("查找失败")
        }
      })
    },
    UploadSectionFile(param) {
      if (this.uploadfolder.path===undefined||this.clickNode===undefined) {
        this.$message.error("请选择文件要上传到哪个文件夹！")
      }
      else {
        const formData = new FormData();
        formData.append("files", param.file)
        const node=this.uploadfolder?this.uploadfolder:this.clickNode
        formData.append("folder",node.path)
        this.request.post("/service/cos/upload", formData).then(res => {
          if (res.code === "200") {
            this.$message.success("上传成功！")
            this.load();
            this.onTreeNodeClick(node)
            this.$refs.tree.store.nodesMap[row.id].expanded = true;

          } else {
            this.$message.error("上传失败！")
          }
        })
      }
    },
    appendNode(node){
      const newChild = {
        id: 1000,
        name: ' ',
        isAddFolder:true,
        children: []
      };
      if (!node.children) {
        this.$set(node, 'children', []);
      }
      node.children.push(newChild);
      this.$refs.tree.store.nodesMap[node.id].expanded = true;

    },
    appendFolder(){
      let node=this.clickNode
      if (!node.isFile){
      let path= node.path
        this.request.get("/service/cos/uploadFile", {
          params: {
            folder: path + "/" +this.folderName,
            nextMarker: this.nextMarker
          }
        }).then(res => {
          if (res.code === "200") {
            this.folderName=''
            this.$message.success("添加成功！")
            this.load();
            this.onTreeNodeClick(node)
          } else {
            this.$message.error("添加成功！")
          }
        })
      }
      else {
        this.$message.error("请勿选择文件进行文件夹添加操作！")
      }
      },
    canceladFolder(){
     this.load()
    },
    del(row) {
      if (row.isFile){
      this.request.get("/service/cos/delete", {
        params: {
          folder: row.path
        }
      }).then(res => {
            if (res.code === '200') {
              this.$message.success("删除成功")
              this.load()
              if (this.uploadfolder.path!==undefined||this.clickNode!==undefined) {
                const node=this.uploadfolder?this.uploadfolder:this.clickNode
                this.onTreeNodeClick(node)
                this.$refs.tree.store.nodesMap[row.id].expanded = true;
              }
            } else {
              this.$message.error("删除失败")
            }
          })

    }
      else {
        this.request.get("/service/cos/deleteFolder", {
          params: {
            folder: row.path
          }
        })
            .then(res => {
              if (res.code === '200') {
                this.$message.success("删除成功")
                this.load()
                if (this.uploadfolder.path!==undefined||this.clickNode!==undefined) {
                  const node=this.uploadfolder?this.uploadfolder:this.clickNode
                  this.onTreeNodeClick(node)
                  this.$refs.tree.store.nodesMap[row.id].expanded = true;

                }
              } else {
                this.$message.error("删除失败")
              }
            })
      }
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
      this.delBtlStatu = val.length == 0
    },

    reset() {
      this.folder = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },

    download(row) {
      console.log(row.path)
      this.request.get("/service/cos/file/download", {
        params: {
          fileName: row.path,
        }
      }).then(res => {
        if (res.code === "200") {
          window.open(res.data.url)
        } else {
          this.$message.error("下载失败")
        }

      })
    },
    delBatch() {

    },

    onTreeNodeClick(node) {
      if (!node.isFile) {
        this.uploadfolder = node;
        this.updateTableData(node);
        this.updateBreadcrumb(node.path);
      } else {
        this.tableData = [node];
        this.updateBreadcrumb(node.path);
      }
    },
    onRowDblClick(row) {
      this.$refs.tree.setCurrentKey(row.id);
      this.$refs.tree.store.nodesMap[row.id].expanded = true;
      if (!row.isFile) {
        const path = [...this.currentPath, row.name];
        this.navigateToPath(path);
      } else {
        console.log(row)
        this.tableData = [row];
        this.updateBreadcrumb(row.path);

      }
    },
    onBreadcrumbClick(index) {
      const path = this.currentPath.slice(0, index + 1);
      this.navigateToPath(path);
    },

    navigateToPath(path) {
      const node = this.findNodeByPath(this.treeData[0], path.join('/'));
      if (node) {
        this.onTreeNodeClick(node)
        this.$refs.tree.setCurrentKey(node.id);

      } else {
        this.$message.error("路径未找到");
      }
    },
    //递归查找特定路径的节点
    findNodeByPath(node, path) {
      if (node.path === path) {
        return node;
      }
      if (node.children) {
        for (let child of node.children) {
          const foundNode = this.findNodeByPath(child, path);
          if (foundNode) {
            return foundNode;
          }
        }
      }
      return null;
    },
    addCard(node){
      this.$refs.upload.$children[0].handleClick();

    },
    updateTableData(node) {
      this.tableData = node.children || [];
      this.currentPath = node.path.split('/');
    },
    updateBreadcrumb(path) {
      this.breadcrumb = path.split('/');
    },

    NodeImg(data) {
      if (!data.isFile) {
        if (data.isOpen) {
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E5%A4%B9.svg"
        } else {
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool/%E5%9B%BE%E6%A0%87/3-%E6%96%87%E4%BB%B6%E5%A4%B9.svg"
        }
      } else {
        return this.dynamicImageUrl(data.type, data.isFile)
      }
    },
    NodeExpandOpen(data, node, expanded) {
      // 如果节点被展开，更新 type 为打开的文件夹
      data.isOpen = true
    },
    NodeExpandClose(data, node, expanded) {
      // 如果节点被关闭，更新 type 为关闭的文件夹
      data.isOpen = false
    },
    dynamicImageUrl(type, isFile) {
      if (!isFile) {
        return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E7%BC%A9%E7%95%A5%E5%9B%BE-%E6%96%87%E4%BB%B6%E5%A4%B9.svg"
      }
      switch (type) {
        case "photo":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E6%A0%87%E5%87%86%E5%9B%BE-%E5%9B%BE%E7%89%87%E6%96%87%E4%BB%B6.svg";
        case "xls":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E6%A0%87%E5%87%86%E5%9B%BE-%E5%B7%A5%E4%BD%9C%E8%A1%A8.svg";

        case "txt":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool/%E5%9B%BE%E6%A0%87/%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E6%A0%87%E5%87%86%E5%9B%BE-%E8%AE%B0%E4%BA%8B%E6%9C%AC.svg"
        case "pdf":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E6%A0%87%E5%87%86%E5%9B%BE-PDF%E6%96%87%E6%A1%A3.svg"
        case "word":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E6%A0%87%E5%87%86%E5%9B%BE-Word%E6%96%87%E6%A1%A3.svg"
        case "ppt":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E6%A0%87%E5%87%86%E5%9B%BE-%E5%B9%BB%E7%81%AF%E7%89%87.svg"
        case "mp3":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E6%A0%87%E5%87%86%E5%9B%BE-%E5%A3%B0%E9%9F%B3%E6%96%87%E4%BB%B6.svg"
        case "mp4":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E6%A0%87%E5%87%86%E5%9B%BE-%E8%A7%86%E9%A2%91%E6%96%87%E4%BB%B6.svg"
        case "zip":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E7%BC%A9%E7%95%A5%E5%9B%BE-%E5%8E%8B%E7%BC%A9%E6%96%87%E4%BB%B6.svg"
        case "exe":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool%2F%E5%9B%BE%E6%A0%87%2F%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E6%A0%87%E5%87%86%E5%9B%BE-%E9%93%BE%E6%8E%A5.svg"
        case "unknown":
          return "https://xiaoku-1312782209.cos.ap-guangzhou.myqcloud.com/xiaocool/%E5%9B%BE%E6%A0%87/%E6%96%87%E4%BB%B6%E7%B1%BB%E5%9E%8B-%E6%A0%87%E5%87%86%E5%9B%BE-%E6%9C%AA%E7%9F%A5%E6%96%87%E4%BB%B6.svg"

      }
    },

    //根据名字寻找路径
    findPathByName(name) {
      const path = [];
      if (this.findNodeByName(this.treeData[0], name, path)) {
        return path;
      }
      return null;
    },
    findNodeByName(node, name, path) {
      if (node.name === name) {
        path.push(node.name);
        this.$refs.tree.store.nodesMap[node.id].expanded = true;
        return true;
      }
      if (node.children) {
        for (let child of node.children) {
          if (this.findNodeByName(child, name, path)) {
            path.unshift(node.name); // 将父节点添加到路径前
            return true;
          }
        }
      }
      return false;
    },
    navigateToName(name) {
      const path = this.findPathByName(name);
      if (path) {
        this.navigateToPath(path);
      } else {
        this.$message.error("节点未找到");
      }
    },


    rightClick(MouseEvent, object, Node, element) { // 鼠标右击触发事件
      this.menuVisible = false // 先把模态框关死，目的是 第二次或者第n次右键鼠标的时候 它默认的是true
      this.menuVisible = true  // 显示模态窗口，跳出自定义菜单栏
      var menu = document.querySelector('#menu')
      // 获取滚动偏移量
      const scrollTop = window.scrollY || document.documentElement.scrollTop;
      const scrollLeft = window.scrollX || document.documentElement.scrollLeft;
      menu.style.left = MouseEvent.clientX + 5 + scrollLeft + 'px'  // MouseEvent.clientX获取鼠标点击的坐标，在该处新增加的menu填充的位置
      menu.style.top = MouseEvent.clientY - 10 + scrollTop +'px'
      document.addEventListener('click', this.foo) // 给整个document添加监听鼠标事件，点击任何位置执行foo方法
      this.clickNode = Node.data  //存储待操作的节点或删除、或在该层或子层添加节点
      // alert(Node.level +":"+Node.label)
    },
    foo() { // 取消鼠标监听事件 菜单栏
      this.menuVisible = false
      document.removeEventListener('click', this.foo) // 要及时关掉监听，不关掉的是一个坑，不信你试试，虽然前台显示的时候没有啥毛病，加一个alert你就知道了
    },


  }
}
</script>

<style scoped lang="scss">
::v-deep .el-tree .el-tree-node__content {
  height: 40px;
  padding: 5px; /* 根据需要调整内边距 */
}


::v-deep .custom-tree-node{
  user-select: none;
  width: 90%;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  justify-content: space-between;
  align-items: center;
  .tree-node-l{
    overflow: hidden !important; // 溢出部分隐藏
    white-space: nowrap !important; //禁止自动换行
    text-overflow: ellipsis !important; // 使溢出部分以省略号显示
    display: block !important;
    font-size: 16px;
    align-items: center;

  }

}

.el-treeSvg {
  height: 16px;
  margin-right: 5px; // 设置右边距
}


::v-deep .el-tree {
  height: 600px;
  resize: none;
  overflow-y: hidden;
  /*定义滚动条宽高及背景，宽高分别对应横竖滚动条的尺寸*/
  // 滚动条整体部分
  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  // 滚动条的轨道的两端按钮，允许通过点击微调小方块的位置。
  &::-webkit-scrollbar-button {
    display: none;
  }

  // 滚动条里面的小方块，能向上向下移动（或往左往右移动，取决于是垂直滚动条还是水平滚动条）
  &::-webkit-scrollbar-thumb {
    background: rgba(144, 147, 153, 0.3);
    cursor: pointer;
    border-radius: 4px;
  }

  // 边角，即两个滚动条的交汇处
  &::-webkit-scrollbar-corner {
    display: none;
  }

  // 两个滚动条的交汇处上用于通过拖动调整元素大小的小控件
  &::-webkit-resizer {
    display: none;
  }

}

::v-deep .el-tree:corner-present {
  scrollbar-width: none;
}

::v-deep .el-tree:hover {
  overflow-y: auto;
  /*定义滚动条宽高及背景，宽高分别对应横竖滚动条的尺寸*/
  scrollbar-width: thin;
  scrollbar-color: #999999 #f0f0f0;


  // 滚动条整体部分
  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  // 滚动条的轨道的两端按钮，允许通过点击微调小方块的位置。
  &::-webkit-scrollbar-button {
    display: none;
  }

  // 滚动条里面的小方块，能向上向下移动（或往左往右移动，取决于是垂直滚动条还是水平滚动条）
  &::-webkit-scrollbar-thumb {
    background: rgba(144, 147, 153, 0.3);
    cursor: pointer;
    border-radius: 4px;
  }

  // 边角，即两个滚动条的交汇处
  &::-webkit-scrollbar-corner {
    display: none;
  }

  // 两个滚动条的交汇处上用于通过拖动调整元素大小的小控件
  &::-webkit-resizer {
    display: none;
  }
}


.menu__item {
  display: block;
  line-height: 20px;
  text-align: center;
  margin-top: 10px;
}

.menu {
  height: 100px;
  width: 120px;
  position: absolute;
  border-radius: 10px;
  border: 1px solid #999999;
  background-color: #f4f4f4;
}

.menu li:hover {
  background-color: #1790ff;
  color: white;
  cursor: pointer;

}


</style>