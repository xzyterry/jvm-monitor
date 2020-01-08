import request from '@/utils/request'

export function sendEmail(data) {
  return request({
    url: '/send/mail',
    method: 'post',
    data
  })
}

export function eventQuery(data) {
  return request({
    url: '/event/query',
    method: 'post',
    data
  })
}
