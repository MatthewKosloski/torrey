package me.mtk.torrey.frontend.ir.addressing;

public interface IRAddress
{
  IRAddressingMode mode();
  Object value();
  IRAddress makeCopy();
}
