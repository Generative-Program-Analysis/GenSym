package sai.common.ai

trait NumAbsDomain {
  type AD

  def +(other: AD): AD
  def -(other: AD): AD
  def *(other: AD): AD
  def /(other: AD): AD
}
