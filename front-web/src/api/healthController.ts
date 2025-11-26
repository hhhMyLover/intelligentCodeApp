// @ts-ignore
/* eslint-disable */
import request from '@/utils/request'

/** 检查服务是否正常 GET /health/check */
export async function check(options?: { [key: string]: any }) {
  return request<string>('/health/check', {
    method: 'GET',
    ...(options || {}),
  })
}
