import request from '@/utils/request'

export function query(data) {
  return request({
    url: '/api/data/jstack',
    method: 'post',
    data
  })
}
