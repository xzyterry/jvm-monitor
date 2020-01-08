<template>
  <div>
    <el-row>
      <el-select v-model="params.hostName" filterable placeholder="请选择服务器">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select v-model="params.serviceName" filterable placeholder="请选择服务">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-date-picker
        v-model="params.dateStr"
        type="date"
        value-format="yyyy-MM-dd"
        placeholder="选择日期"
      />
      <el-time-picker
        v-model="params.minStr"
        format="HH:mm"
        value-format="HH:mm"
        placeholder="选择时间范围"
      />
      <el-button icon="el-search" type="primary" @click="handleQuery">查询</el-button>
    </el-row>

    <el-row>
      <el-card class="box-card">
        <div id="online" style="height:400px; margin-top:10px" />
      </el-card>
    </el-row>

  </div>
</template>

<script>
import { query } from '@/api/jstack.js'
export default {
  data() {
    return {
      params: {
        dateStr: '',
        minStr: '',
        hostName: '',
        serviceName: ''
      },
      data: [],
      type: []
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    handleQuery() {
      query({ params: this.params })
        .then(resolve => {
          var data = resolve.data
          if (data == null || data.length < 1) {
            return
          }

          data.forEach(item => {
            var obj = {}
            obj.value = item.count
            var state = item.state == null ? '其他 ' : item.state
            obj.name = state
            this.data.push(obj)
            this.type.push(state)
          })
          this.init()
        })
        .catch(err => {
          console.log(err)
        })
    },
    init() {
      var myChart = this.$echarts.init(document.getElementById('online'))
      var option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        title: {
          text: '线程状态'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          data: this.type
        },
        series: [
          {
            name: '线程状态',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false,
                position: 'center'
              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: '20',
                  fontWeight: 'bold'
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: this.data
          }
        ]
      }

      myChart.setOption(option, true)
    }
  }
}
</script>

<style lang="scss">
.box-card{
  width: 90%
}

.el-row{
  margin: 10px 20px;
}

</style>
