<template>
  <div>
    <el-row>
      <el-select v-model="hostName" filterable placeholder="请选择服务器">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select v-model="serviceName" filterable placeholder="请选择服务">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-date-picker
        v-model="dateStr"
        type="date"
        value-format="yyyy-MM-dd"
        placeholder="选择日期"
      />
      <el-time-picker
        v-model="minStr"
        format="HH:mm"
        value-format="HH:mm"
        placeholder="选择时间范围"
      />
      <el-button icon="el-search" type="primary">查询</el-button>
    </el-row>

    <el-row>
      <el-card class="box-card">
        <div id="online" style="height:400px; margin-top:10px" />
      </el-card>
    </el-row>

  </div>
</template>

<script>
export default {
  data() {
    return {
      dateStr: '',
      minStr: '',
      hostName: '',
      serviceName: ''
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      var myChart = this.$echarts.init(document.getElementById('online'))
      var option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 10,
          data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
        },
        series: [
          {
            name: '访问来源',
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
                  fontSize: '30',
                  fontWeight: 'bold'
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: [
              { value: 335, name: '直接访问' },
              { value: 310, name: '邮件营销' },
              { value: 234, name: '联盟广告' },
              { value: 135, name: '视频广告' },
              { value: 1548, name: '搜索引擎' }
            ]
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
