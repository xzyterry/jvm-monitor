import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/data/online',
    method: 'post',
    params: query
  })
}

export function fetchGoldEggList(query) {
  return request({
    url: '/data/room/money',
    method: 'post',
    params: query
  })
}

export function fetchHistory(query) {
  return request({
    url: '/data/online/total/mul',
    method: 'post',
    data: query
  })
}
