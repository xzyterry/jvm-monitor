export default [

  {
    url: '/api/data/jstack',
    type: 'post',
    response: (data) => {
      return {
        'resultCode': 0,
        'data': [
          {
            'dateStr': '2020-01-08',
            'minStr': '2020-01-08 20:08:00',
            'hostName': 'ws-old',
            'serviceName': 'dashboard-service',
            'state': null,
            'count': 7,
            'createDate': '2020-01-08T12:09:00.000+0000'
          },
          {
            'dateStr': '2020-01-08',
            'minStr': '2020-01-08 20:08:00',
            'hostName': 'ws-old',
            'serviceName': 'dashboard-service',
            'state': 'WAITING (on object monitor)',
            'count': 2,
            'createDate': '2020-01-08T12:09:00.000+0000'
          },
          {
            'dateStr': '2020-01-08',
            'minStr': '2020-01-08 20:08:00',
            'hostName': 'ws-old',
            'serviceName': 'dashboard-service',
            'state': 'BLOCKED (on object monitor)',
            'count': 2,
            'createDate': '2020-01-08T12:09:00.000+0000'
          },
          {
            'dateStr': '2020-01-08',
            'minStr': '2020-01-08 20:08:00',
            'hostName': 'ws-old',
            'serviceName': 'dashboard-service',
            'state': 'WAITING (parking)',
            'count': 5,
            'createDate': '2020-01-08T12:09:00.000+0000'
          },
          {
            'dateStr': '2020-01-08',
            'minStr': '2020-01-08 20:08:00',
            'hostName': 'ws-old',
            'serviceName': 'dashboard-service',
            'state': 'RUNNABLE',
            'count': 17,
            'createDate': '2020-01-08T12:09:00.000+0000'
          },
          {
            'dateStr': '2020-01-08',
            'minStr': '2020-01-08 20:08:00',
            'hostName': 'ws-old',
            'serviceName': 'dashboard-service',
            'state': 'TIMED_WAITING (on object monitor)',
            'count': 1,
            'createDate': '2020-01-08T12:09:00.000+0000'
          },
          {
            'dateStr': '2020-01-08',
            'minStr': '2020-01-08 20:08:00',
            'hostName': 'ws-old',
            'serviceName': 'dashboard-service',
            'state': 'TIMED_WAITING (sleeping)',
            'count': 1,
            'createDate': '2020-01-08T12:09:00.000+0000'
          },
          {
            'dateStr': '2020-01-08',
            'minStr': '2020-01-08 20:08:00',
            'hostName': 'ws-old',
            'serviceName': 'dashboard-service',
            'state': 'TIMED_WAITING (parking)',
            'count': 19,
            'createDate': '2020-01-08T12:09:00.000+0000'
          }
        ],
        'message': null
      }
    }
  }

]
